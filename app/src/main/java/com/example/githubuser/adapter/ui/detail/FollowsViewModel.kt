package com.example.githubuser.adapter.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.ItemsItem
import com.example.githubuser.network.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowsViewModel : ViewModel() {
    private val _follows = MutableLiveData<ArrayList<ItemsItem>?>()
    val follows: LiveData<ArrayList<ItemsItem>?> = _follows

    private val _isFollowsNull = MutableLiveData<Boolean>()
    val isFollowsNull: LiveData<Boolean> = _isFollowsNull

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowers(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersFollower(user)
        client.enqueue(object : Callback<ArrayList<ItemsItem>> {
            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.isEmpty()) {
                            _isFollowsNull.postValue(true)
                        } else {
                            _follows.postValue(responseBody)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersFollowing(user)
        client.enqueue(object : Callback<ArrayList<ItemsItem>> {
            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.isEmpty()) {
                            _isFollowsNull.postValue(true)
                        } else {
                            _follows.postValue(responseBody)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        const val TAG = "FollowsViewModel"
    }
}