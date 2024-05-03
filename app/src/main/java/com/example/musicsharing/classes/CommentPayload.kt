package com.example.musicsharing.classes

data class CommentPayload (
    val comment: String,
    val postId: Int,
    val userId: Int
)