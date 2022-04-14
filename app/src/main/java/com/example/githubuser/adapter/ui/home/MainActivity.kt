package com.example.githubuser.adapter.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.githubuser.ItemsItem
import com.example.githubuser.R
import com.example.githubuser.network.database.SettingPreferences
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.adapter.ui.detail.DetailActivity
import com.example.githubuser.adapter.ui.favorite.FavoriteActivity
import com.example.githubuser.adapter.ui.setting.SettingActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListUserAdapter by lazy {
        ListUserAdapter()
    }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            SettingPreferences.getInstance(dataStore)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getThemeSetting()
        observeAnimationAndProgressBar()
        getUserItems()
    }

    private fun getThemeSetting() {
        mainViewModel.getThemeSettings().observe(this@MainActivity) { selectedTheme: Int ->
            when (selectedTheme) {
                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                showProgressBar(true)
                mainViewModel.getSearchUser(query)
                mainViewModel.searchUser.observe(this@MainActivity) {
                    if (it != null) {
                        adapter.setList(it)
                        listUserData()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite ->{
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.setting ->{
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getUserItems() {
        mainViewModel.listUser.observe(this@MainActivity) {
            if (it != null) {
                adapter.setList(it)
                listUserData()
            }
        }
    }

    private fun listUserData() {
        binding.rvHeroes.setHasFixedSize(true)
        binding.rvHeroes.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClick(user: ItemsItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    it.putExtra(DetailActivity.EXTRA_ID, user.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, user.avatarUrl)
                    startActivity(it)
                }
            }
        })
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun observeAnimationAndProgressBar() {
        mainViewModel.isLoading.observe(this@MainActivity) {
            showProgressBar(it)
        }
    }
}