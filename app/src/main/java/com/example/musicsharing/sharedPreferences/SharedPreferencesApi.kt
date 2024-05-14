package com.example.musicsharing.sharedPreferences

import android.content.SharedPreferences
import com.example.musicsharing.constants.SharedPreferencesConstants
import android.content.Context
import com.example.musicsharing.MusicSharing

object SharedPreferencesApi {
    private val sharedPreferences: SharedPreferences = MusicSharing.instance.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    fun getIsLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString("username", "")
    }

    fun getSpotifyID(): String? {
        return sharedPreferences.getString("spotifyID", "")
    }

    fun editTokens(token: String, refreshToken: String){
        sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_TOKEN, token).apply()
        sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_REFRESH_TOKEN, refreshToken).apply()
    }

    fun addNewUserInfo(spotifyId: String, username: String){
        sharedPreferences.edit().putBoolean(SharedPreferencesConstants.KEY_LOGGED_IN, true).apply()
        sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_SPOTIFY_ID, spotifyId).apply()
        sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_USERNAME, username).apply()
    }
    fun signOut() {
        sharedPreferences.edit().putBoolean(SharedPreferencesConstants.KEY_LOGGED_IN, false).apply()
        sharedPreferences.edit().remove(SharedPreferencesConstants.KEY_SPOTIFY_ID).apply()
        sharedPreferences.edit().remove(SharedPreferencesConstants.KEY_TOKEN).apply()
        sharedPreferences.edit().remove(SharedPreferencesConstants.KEY_REFRESH_TOKEN).apply()
        sharedPreferences.edit().remove(SharedPreferencesConstants.KEY_USERNAME).apply()
    }
}