package com.example.githubuser.adapter.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubuser.ItemsItem
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(
            application, "someString"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        if (username != null) {
            getDetailUser(username)
            setTabLayout(username)
        }

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                detailViewModel.addToFavorite(id, username!!, avatarUrl!!)
            } else {
                detailViewModel.deleteUser(id)
            }
            binding.toggleFavorite.isChecked = isChecked
        }
    }

    private fun getDetailUser(username: String) {
        val detailViewModel: DetailViewModel by viewModels {
            DetailViewModelFactory(application, username)
        }
        detailViewModel.detailUser.observe(this@DetailActivity) {
            if (it != null) {
                setUserData(it)
            }

        }
        detailViewModel.isLoading.observe(this@DetailActivity) {
            showsLoading(it)
        }
    }

    private fun setUserData(user: ItemsItem) {
        with(binding) {
            tvUsername.text = user.login
            tvName.text = user.name
            tvFollowers.text = "${user.followers}"
            tvFollowing.text = "${user.following}"
            tvRepository.text = user.repository
            tvCompany.text = user.company
            tvLocation.text = user.location
            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.imgAvatar)
        }
    }

    private fun setTabLayout(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.viewPager.offscreenPageLimit = 1
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showsLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }
}