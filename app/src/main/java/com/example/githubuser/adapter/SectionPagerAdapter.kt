package com.example.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.adapter.ui.detail.FollowsFragment

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var username: String? = null

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowsFragment()
        fragment.arguments = Bundle().apply {
            putString(FollowsFragment.ARG_USERNAME, username)
            putInt(FollowsFragment.ARG_NUMBER, position)
        }
        return fragment
    }

    override fun getItemCount(): Int = 2
}