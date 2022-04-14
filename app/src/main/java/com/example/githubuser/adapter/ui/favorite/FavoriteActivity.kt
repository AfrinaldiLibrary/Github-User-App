package com.example.githubuser.adapter.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.githubuser.Favorite
import com.example.githubuser.adapter.FavoriteUserAdapter
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.network.database.FavoriteUser
import com.example.githubuser.adapter.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    private val adapter: FavoriteUserAdapter by lazy {
        FavoriteUserAdapter()
    }

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite Page"

        favoriteViewModel.getFavoriteUser()?.observe(this@FavoriteActivity) {
            if (it != null){
                val list = mapList(it)
                adapter.setList(list)
            }
        }
        listUserData()
    }

    private fun mapList(users : List<FavoriteUser>) : ArrayList<Favorite>{
        val listUser = ArrayList<Favorite>()
        for (user in users){
            val userMapped = Favorite(
                user.id,
                user.login,
                user.avatarUrl
            )
            listUser.add(userMapped)
        }
        return listUser
    }

    private fun listUserData() {
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter
        adapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback {
            override fun onItemClick(user: Favorite) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    it.putExtra(DetailActivity.EXTRA_ID, user.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, user.avatarUrl)
                    startActivity(it)
                }
            }
        })
    }
}