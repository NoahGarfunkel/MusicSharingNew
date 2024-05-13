package com.example.musicsharing.retrofit

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.musicsharing.constants.SharedPreferencesConstants
import com.example.musicsharing.retrofit.api.AccountsApi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.example.musicsharing.BuildConfig

class AccountsRetrofit() {

    private val baseUrl = "https://accounts.spotify.com/"

    private val okHttpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .build()

    private val accountsApi = getInstance().create(AccountsApi::class.java)

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun updateToken(sharedPreferences: SharedPreferences){
        val clientID = BuildConfig.SPOTIFY_CLIENT_ID
        val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET

        val refreshToken = sharedPreferences.getString(SharedPreferencesConstants.KEY_REFRESH_TOKEN, "")
        val authString = "Basic " + Base64.encodeToString("$clientID:$clientSecret".toByteArray(), Base64.NO_WRAP)
        accountsApi.getNewToken(authString,"application/x-www-form-urlencoded","refresh_token", refreshToken!!).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseJSON = JSONObject(response.body()!!.string())
                    val newToken = responseJSON.getString("access_token")
                    sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_TOKEN, newToken).apply()
                } else {
                    Log.e("retryGetSongsList", "getRefreshToken request failed with code: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("retryGetSongsList", "getRefreshToken request failed: ${t.message}")
            }
        })
    }
}