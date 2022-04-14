package com.example.githubuser

import com.google.gson.annotations.SerializedName

data class GitHubResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: ArrayList<ItemsItem>,
)

data class ItemsItem(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("name")
	val name : String,

	@field:SerializedName("company")
	val company : String,

	@field:SerializedName("location")
	val location : String,

	@field:SerializedName("public_repos")
	val repository : String,

	@field:SerializedName("followers")
	val followers : Int,

	@field:SerializedName("following")
	val following : Int
)

data class Favorite(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String
)
