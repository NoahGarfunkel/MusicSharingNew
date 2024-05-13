package com.example.musicsharing.activities

import ProfileScreen
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.musicsharing.models.Track
import com.example.musicsharing.constants.SharedPreferencesConstants
import com.example.musicsharing.displayScreens.AccountCreationScreen
import com.example.musicsharing.displayScreens.LoginScreen
import com.example.musicsharing.displayScreens.PostsScreen
import com.example.musicsharing.navigation.MainAppNavigation
import com.example.musicsharing.navigation.Screens
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.retrofit.WebRetrofit
import com.example.musicsharing.retrofit.api.WebApi
import com.example.musicsharing.sharedPreferences.AppSharedPreferences
import com.example.musicsharing.ui.theme.MusicSharingTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private val webApi = WebRetrofit().getInstance().create(WebApi::class.java)
    private val accountsRetrofit = AccountsRetrofit()
    private val appSharedPreferences = AppSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicSharingTheme {
                var startScreen = "LoginNavigation"
                val uri = intent.data
                if (uri != null && uri.scheme == "music-sharing" && uri.host == "redirect") {
                    val path = uri.path
                    if (path == "/main-activity/account-creation") {
                        val code = uri.getQueryParameter("code")
                        startScreen = ("AccountCreation/${code}")
                    }
                }
                if (appSharedPreferences.getIsLoggedIn()) {
                    startScreen = "PostsScreen"
                }
                MainAppNavigation(startScreen)
            }
        }
    }

        /*private suspend fun getSongsList(input: String): List<Track> {
        val tracksList = mutableListOf<Track>()
        val token = sharedPreferences.getString(SharedPreferencesConstants.KEY_TOKEN, "")
        return withContext(Dispatchers.IO) {
            try {
                val response = webApi.getSongs("Bearer $token", input).execute()
                if (response.isSuccessful && response.body() != null) {
                    val jsonArray = JSONObject(response.body()!!.string()).getJSONObject("tracks").getJSONArray("items")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val trackName = jsonObject.optString("name")
                        val trackID = jsonObject.optString("id")
                        val track = Track(trackID, trackName)
                        tracksList.add(track)
                    }
                    tracksList
                } else {
                    if(response.code() == 401){
                        accountsRetrofit.updateToken(sharedPreferences, context)
                        getSongsList(input)
                    }
                    else{
                        Log.e("getSongsList", "getSongsList request failed with code: ${response.code()}")
                        emptyList<Track>()
                    }
                }
            } catch (e: Exception) {
                Log.e("getSongsList", "getSongsList request failed: ${e.message}")
                emptyList<Track>()
            }
        }
    }*/
}