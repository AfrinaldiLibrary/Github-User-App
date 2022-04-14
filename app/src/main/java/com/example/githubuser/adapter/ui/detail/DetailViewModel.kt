package com.example.githubuser.adapter.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.ItemsItem
import com.example.githubuser.network.api.ApiConfig
import com.example.githubuser.network.database.FavoriteUser
import com.example.githubuser.network.database.FavoriteUserDao
import com.example.githubuser.network.database.FavoriteUserRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(application: Application, username: String) : AndroidViewModel(application) {
    private val _detailUser = MutableLiveData<ItemsItem>()
    val detailUser: LiveData<ItemsItem> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var userDao: FavoriteUserDao?
    private var userDb: FavoriteUserRoomDatabase?

    init {
        viewModelScope.launch { getUserData(username) }
        userDb = FavoriteUserRoomDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    private fun getUserData(username: String) {
        coroutineScope.launch {
            _isLoading.value = true
            val result = ApiConfig.getApiService().getDetailUser(username)
            try {
                _isLoading.value = false
                _detailUser.postValue(result)
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }
        }
    }

    fun addToFavorite(id: Int, name : String, avatar : String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                id,
                name,
                avatar
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun deleteUser(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.deleteUser(id)
        }
    }

    companion object {
        const val TAG = "DetailViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

