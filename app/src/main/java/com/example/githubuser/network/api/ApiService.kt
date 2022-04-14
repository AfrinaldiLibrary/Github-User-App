package com.example.githubuser.network.api

import com.example.githubuser.BuildConfig.KEY
import com.example.githubuser.GitHubResponse
import com.example.githubuser.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    @Headers("Authorization: token $KEY")
    suspend fun getListUser(): ArrayList<ItemsItem>

    @GET("users/{username}")
    @Headers("Authorization: token $KEY")
    suspend fun getDetailUser(@Path("username") username: String): ItemsItem

    @GET("search/users")
    @Headers("Authorization: token $KEY")
    fun getSearchUser(@Query("q") username: String): Call<GitHubResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $KEY")
    fun getUsersFollower(@Path("username") username: String) : Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $KEY")
    fun getUsersFollowing(@Path("username") username: String) : Call<ArrayList<ItemsItem>>
}