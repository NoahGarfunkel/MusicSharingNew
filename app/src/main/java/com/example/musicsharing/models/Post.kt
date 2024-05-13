package com.example.musicsharing.models

import java.time.LocalDateTime

data class Post(
    val artistName: String,
    val caption: String,
    val comments: List<Comment>?,
    val createdOn: LocalDateTime,
    val imageUrl: String,
    val isLikedByUser: Boolean,
    val likeTotal: Int,
    val spotifyId: String,
    val spotifyUrl: String,
    val trackName: String,
    val userId: Int,
    val userName: String
)
