package com.example.musicsharing.activities

import PropertiesReader
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.example.musicsharing.classes.User
import com.example.musicsharing.classes.UserInfoPayload
import com.example.musicsharing.constants.SharedPreferencesConstants
import com.example.musicsharing.retrofit.api.AccountsApi
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.retrofit.BackendRetrofit
import com.example.musicsharing.retrofit.api.WebApi
import com.example.musicsharing.retrofit.WebRetrofit
import com.example.musicsharing.retrofit.api.BackendApi
import com.example.musicsharing.ui.theme.MusicSharingTheme
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountCreationActivity : ComponentActivity() {
    private lateinit var clientID: String
    private lateinit var clientSecret: String
    private var code: String? = null
    private val accountsApi = AccountsRetrofit().getInstance().create(AccountsApi::class.java)
    private val webApi = WebRetrofit().getInstance().create(WebApi::class.java)
    private val backendApi = BackendRetrofit().getInstance().create(BackendApi::class.java)
    private val currentActivity = this
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        PropertiesReader.init(this)
        clientID = PropertiesReader.getProperty("SPOTIFY_CLIENT_ID")
        clientSecret = PropertiesReader.getProperty("SPOTIFY_CLIENT_SECRET")
        val uri: Uri? = intent.data

        if (uri != null && uri.scheme == "music-sharing") {
            code = uri.getQueryParameter("code")
        }

        setContent{
            MusicSharingTheme {
                AccountCreationScreen()
            }
        }
    }

    @Composable
    fun AccountCreationScreen(){
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
                        code?.let { getToken(it, userName) }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }
    }

    private fun getToken(code: String, userName: String){
        val authString = "Basic " + Base64.encodeToString("$clientID:$clientSecret".toByteArray(), Base64.NO_WRAP)
        val tokenCall = accountsApi.getToken(authString, "application/x-www-form-urlencoded", "authorization_code", code, "music-sharing://account-creation")

        tokenCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseJSON = JSONObject(response.body()!!.string())
                    val token = responseJSON.getString("access_token")
                    val refreshToken = responseJSON.getString("refresh_token")
                    sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_TOKEN, token).apply()
                    sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_REFRESH_TOKEN, refreshToken).apply()

                    saveUserInfo(token, userName)
                } else {
                    Log.e("getToken", "API call failed with code: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("getToken", "API call failed with exception: ${t.message}")
            }
        })
    }

    private fun saveUserInfo(token: String, userName: String){
        val userCall = webApi.getUser("Bearer $token")

        userCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonObject = JSONObject(response.body()!!.string())
                    val spotifyID = jsonObject.optString("id")
                    val userInfo = UserInfoPayload(spotifyID, userName)
                    if (response.body() != null) {
                        backendApi.saveUserInfo(userInfo).enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.isSuccessful) {
                                    sharedPreferences.edit().putBoolean(SharedPreferencesConstants.KEY_LOGGED_IN, true).apply()
                                    sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_SPOTIFY_ID, spotifyID).apply()
                                    sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_USER_NAME, userName).apply()
                                    setUserId(spotifyID)
                                    startActivity(Intent(currentActivity, NavigationActivity::class.java))

                                } else {
                                    Log.e("Response", "saveUserInfo request failed with code: ${response.errorBody()?.string()}")

                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                sharedPreferences.edit().putBoolean(SharedPreferencesConstants.KEY_LOGGED_IN, true).apply()
                                sharedPreferences.edit().putString(SharedPreferencesConstants.KEY_SPOTIFY_ID, spotifyID).apply()
                                setUserId(spotifyID)
                                startActivity(Intent(currentActivity, NavigationActivity::class.java))
                                Log.e("saveUserInfo", "saveUserInfo request failed: ${t.message}")
                            }
                        })
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

    private fun setUserId(spotifyId: String) {
        backendApi.getUser(spotifyId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null && response.body()!!.id != 0) {
                    val userId = response.body()!!.id
                    sharedPreferences.edit().putInt(SharedPreferencesConstants.KEY_USER_ID, userId).apply()
                    Log.d("getUserResponse", "getUser responded with ${response.body()!!.id}")
                } else {
                    Log.e("Response", "getUser request failed with code: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                sharedPreferences.edit().putInt(SharedPreferencesConstants.KEY_USER_ID, 0).apply()
                Log.e("getUser", "getUser request failed: ${t.message}")
            }
        })
    }
}

