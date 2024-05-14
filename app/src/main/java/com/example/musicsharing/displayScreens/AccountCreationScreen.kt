package com.example.musicsharing.displayScreens

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.musicsharing.BuildConfig
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.retrofit.WebRetrofit
import com.example.musicsharing.retrofit.api.AccountsApi
import com.example.musicsharing.retrofit.api.WebApi
import com.example.musicsharing.sharedPreferences.SharedPreferencesApi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AccountCreationScreen(onNavigateToPosts: () -> Unit, code: String?){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var userName by rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .padding(16.dp, 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Insert User Info",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = "Display name",
                style = MaterialTheme.typography.bodyLarge
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = userName,
                onValueChange = { userName = it },
                placeholder = { Text(text = "e.g. NoahGarfunkel") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()})
            )

            Button(

                onClick = {
                    if (code != null) {
                        getToken(onNavigateToPosts, code, userName)
                    } else{
                        Log.e("Submit", "No code found")
                    }},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}

fun getToken(onNavigationToFriendsList: () -> Unit, code: String, username: String){
    val clientID = BuildConfig.SPOTIFY_CLIENT_ID
    val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
    val authString = "Basic " + Base64.encodeToString("$clientID:$clientSecret".toByteArray(), Base64.NO_WRAP)
    val accountsApi = AccountsRetrofit().getInstance().create(AccountsApi::class.java)
    val tokenCall = accountsApi.getToken(authString, "application/x-www-form-urlencoded", "authorization_code", code, "music-sharing://redirect/main-activity/account-creation")

    tokenCall.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                val responseJSON = JSONObject(response.body()!!.string())
                val token = responseJSON.getString("access_token")
                val refreshToken = responseJSON.getString("refresh_token")
                SharedPreferencesApi.editTokens(token, refreshToken)

                saveUserInfo(onNavigationToFriendsList, token, username)
            } else {
                Log.e("getToken", "API call failed with code: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("getToken", "API call failed with exception: ${t.message}")
        }
    })
}
fun saveUserInfo(onNavigateToAppNavigation: () -> Unit, token: String, username: String){
    val webApi = WebRetrofit().getInstance().create(WebApi::class.java)
    val userCall = webApi.getUser("Bearer $token")

    userCall.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                val jsonObject = JSONObject(response.body()!!.string())
                val spotifyID = jsonObject.optString("id")
                if (response.body() != null) {
                    SharedPreferencesApi.addNewUserInfo(spotifyID,username)
                    onNavigateToAppNavigation()
                }
            } else {
                Log.e("saveUserInfo", "API call failed with code: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("saveUserInfo", "API call failed with exception: ${t.message}")
        }
    })
}