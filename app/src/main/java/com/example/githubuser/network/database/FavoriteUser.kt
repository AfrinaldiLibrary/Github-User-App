package com.example.githubuser.network.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_user")
data class FavoriteUser(
    @PrimaryKey
    val id: Int,

    val login: String,

    val avatarUrl: String
): Serializable