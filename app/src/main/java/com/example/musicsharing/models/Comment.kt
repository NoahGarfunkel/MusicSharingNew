package com.example.musicsharing.models

import java.util.Date

data class Comment(
    val comment: String,
    val commentId: Int,
    val createdOn: Date,
    val userId: String,
    val userName: String
)
