package com.example.musicsharing.retrofit.api

import com.example.musicsharing.models.CommentPayload
import com.example.musicsharing.models.PostPayload
import com.example.musicsharing.models.FollowPayload
import com.example.musicsharing.models.Post
import com.example.musicsharing.models.User
import com.example.musicsharing.models.UserInfoPayload
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BackendApi {
    // User Functions
    @POST("user/follower")
    fun followUser(
        @Body() followPayload: FollowPayload
    ): Call<ResponseBody>

    @GET("user/spotify/{spotifyId}")
    fun getUser(
        @Path("spotifyId") spotifyId: String
    ): Call<User>

    @POST("user/register")
    fun saveUserInfo(
        @Body() userInfo: UserInfoPayload
    ): Call<ResponseBody>

    @GET("user/{spotifyId}/exists/")
    fun userExists(@Path("spotifyId") spotifyId: String): Call<Boolean>

    // Post Functions

    @POST("post/comment")
    fun createComment(
        @Body() commentPayload: CommentPayload
    ): Call<ResponseBody>

    @POST("post")
    fun createPost(
        @Body() postPayload: PostPayload
    ): Call<Post>

    @GET("post/feed/user/{userId}")
    fun getPostFeed(
        @Path("userId") userId: Int
    ): Call<List<Post>>

    @GET("post/{postId}/user/{userId}/like{isLiked}")
    fun likePost(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int,
        @Path("isLiked") isLiked: Boolean
    ): Call<Boolean>
}

