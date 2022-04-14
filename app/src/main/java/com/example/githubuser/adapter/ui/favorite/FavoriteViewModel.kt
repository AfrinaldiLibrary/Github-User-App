package com.example.githubuser.adapter.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.network.database.FavoriteUser
import com.example.githubuser.network.database.FavoriteUserDao
import com.example.githubuser.network.database.FavoriteUserRoomDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDb: FavoriteUserRoomDatabase? = FavoriteUserRoomDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getUser()
    }
}