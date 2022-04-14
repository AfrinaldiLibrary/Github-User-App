package com.example.githubuser.adapter.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.githubuser.ItemsItem
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.databinding.FragmentFollowsBinding

class FollowsFragment : Fragment() {

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!

    private val followsViewModel by viewModels<FollowsViewModel>()

    private val adapter: ListUserAdapter by lazy {
        ListUserAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getString(ARG_USERNAME)

        viewPagerItem(user)
        getItem()
    }

    private fun getItem() {

        followsViewModel.follows.observe(viewLifecycleOwner){
            if (it != null){
                adapter.setList(it)
                setListFollowData()
            }
        }

        followsViewModel.isFollowsNull.observe(viewLifecycleOwner){
            showEmptyMessage(it)
        }

        followsViewModel.isLoading.observe(viewLifecycleOwner) {
            showsLoading(it)
        }
    }

    private fun viewPagerItem(user : String?){
        when (arguments?.getInt(ARG_NUMBER, 0)) {
            0 -> {
                followsViewModel.getFollowers(user!!)
            }
            1 -> {
                followsViewModel.getFollowing(user!!)
            }
        }
    }

    private fun setListFollowData() {
        binding.rvUserFollow.adapter = adapter
        binding.rvUserFollow.setHasFixedSize(true)
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClick(user: ItemsItem) {
                activity?.let { Fragment ->
                    Intent(Fragment, DetailActivity::class.java).also {
                        it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                        it.putExtra(DetailActivity.EXTRA_ID, user.id)
                        it.putExtra(DetailActivity.EXTRA_AVATAR, user.avatarUrl)
                        startActivity(it)
                    }
                }
            }
        })
    }

    private fun showEmptyMessage(isData: Boolean) {
        binding.emptyMessage.visibility = if (isData) View.VISIBLE else View.GONE
    }

    private fun showsLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_NUMBER = "arg_number"
    }
}