package com.example.musicsharing.models

data class CommentPayload (
    val comment: String,
    val postId: Int,
    val userId: Int
)