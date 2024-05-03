package com.example.musicsharing.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebRetrofit() {

    private val baseUrl = "https://api.spotify.com/"

    private val okHttpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .build()
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}