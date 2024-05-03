package com.example.musicsharing.activities

import PropertiesReader
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.musicsharing.retrofit.api.AccountsApi
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.ui.theme.MusicSharingTheme
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val KEY_LOGGED_IN = "isLoggedIn"

class LoginActivity : ComponentActivity() {
    private lateinit var clientID: String
    private lateinit var sharedPreferences: SharedPreferences
    private val accountsApi = AccountsRetrofit().getInstance().create(AccountsApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PropertiesReader.init(this)
        clientID = PropertiesReader.getProperty("SPOTIFY_CLIENT_ID")

        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean(KEY_LOGGED_IN, false)) {
            startActivity(Intent(this, NavigationActivity::class.java))
        } else {
            setContent{
                MusicSharingTheme {
                    LoginScreen()
                }
            }
        }
    }

    @Composable
    fun LoginScreen(){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginButton(onClick = { authorization() })
        }
    }

    @Composable
    fun LoginButton(onClick: () -> Unit) {
        OutlinedButton(onClick = { onClick() }) {
            Text(
                text = "Login with Spotify",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    private fun authorization() {
        val authCall = accountsApi.requestAuth(clientID, "code", "music-sharing://account-creation")
        authCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.raw().request.url.toString()))
                    startActivity(browserIntent)
                } else {
                    Log.e("authorization", "API call failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("authorization", "API call failed with exception: ${t.message}")
            }
        })
    }
}

