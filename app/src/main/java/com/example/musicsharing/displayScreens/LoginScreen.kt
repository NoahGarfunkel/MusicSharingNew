package com.example.musicsharing.displayScreens

import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.example.musicsharing.BuildConfig
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.retrofit.api.AccountsApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uriHandler = LocalUriHandler.current
        OutlinedButton(onClick = { authorization(uriHandler) }) {
            Text(
                text = "Login with Spotify",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

fun authorization(uriHandler: UriHandler) {
    val accountsApi = AccountsRetrofit().getInstance().create(AccountsApi::class.java)
    val clientID = BuildConfig.SPOTIFY_CLIENT_ID

    val authCall = accountsApi.requestAuth(clientID, "code", "music-sharing://redirect/main-activity/account-creation")
    authCall.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                uriHandler.openUri(response.raw().request.url.toString())
            } else {
                Log.e("authorization", "API call failed with code: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("authorization", "API call failed with exception: ${t.message}")
        }
    })
}