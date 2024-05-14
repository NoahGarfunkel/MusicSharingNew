package com.example.musicsharing.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.musicsharing.navigation.AppNavigation
import com.example.musicsharing.ui.theme.MusicSharingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicSharingTheme {
                AppNavigation(intent.data)
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