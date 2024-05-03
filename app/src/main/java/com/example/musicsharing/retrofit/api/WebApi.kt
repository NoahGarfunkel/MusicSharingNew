package com.example.musicsharing.retrofit.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WebApi {
    @GET("v1/me")
    fun getUser(@Header("Authorization") authorization: String): Call<ResponseBody>

    @GET("v1/search?&type=track&limit=10")
    fun getSongs(@Header("Authorization") authorization: String,
                 @Query("q") searchInput: String): Call<ResponseBody>
}