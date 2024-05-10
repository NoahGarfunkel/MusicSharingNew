package com.example.musicsharing.displayScreens

import android.content.Intent
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
import com.example.musicsharing.BuildConfig
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.retrofit.api.AccountsApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    val accountsApi = AccountsRetrofit().getInstance().create(AccountsApi::class.java)
    val clientID = BuildConfig.SPOTIFY_CLIENT_ID

    val authCall = accountsApi.requestAuth(clientID, "code", "music-sharing://account-creation")
    authCall.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.raw().request.url.toString()))
                //startActivity(browserIntent)
            } else {
                Log.e("authorization", "API call failed with code: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("authorization", "API call failed with exception: ${t.message}")
        }
    })
}