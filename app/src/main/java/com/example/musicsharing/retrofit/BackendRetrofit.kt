package com.example.musicsharing.retrofit

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.musicsharing.retrofit.extensions.LocalDateTimeDeserializer
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class BackendRetrofit() {

    private val baseUrl = "http://10.0.2.2:5056/"

    @RequiresApi(Build.VERSION_CODES.O)
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .create()

    private val okHttpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}