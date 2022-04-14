package com.example.githubuser.adapter.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.GitHubResponse
import com.example.githubuser.ItemsItem
import com.example.githubuser.network.database.SettingPreferences
import com.example.githubuser.network.api.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _listUser = MutableLiveData<ArrayList<ItemsItem>>()
    val listUser: LiveData<ArrayList<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchUser = MutableLiveData<ArrayList<ItemsItem>>()
    val searchUser: LiveData<ArrayList<ItemsItem>> = _searchUser

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        viewModelScope.launch { getListUser() }
    }

    private suspend fun getListUser() {
        coroutineScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getListUser()
            try {
                _isLoading.value = false
                _listUser.postValue(client)
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }
        }
    }

    fun getSearchUser(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(user)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _searchUser.postValue(responseBody.items)
                    }
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getThemeSettings(): LiveData<Int> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(themeMode: Int) {
        viewModelScope.launch {
            pref.saveThemeSetting(themeMode)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}