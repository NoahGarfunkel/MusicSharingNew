package com.example.musicsharing.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicsharing.classes.Track
import com.example.musicsharing.constants.SharedPreferencesConstants
import com.example.musicsharing.displayScreens.FriendsScreen
import com.example.musicsharing.displayScreens.GreetingsScreen
import com.example.musicsharing.displayScreens.FeedScreen
import com.example.musicsharing.displayScreens.LoginScreen
import com.example.musicsharing.navigation.Screens
import com.example.musicsharing.navigation.listOfNavItems
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.retrofit.WebRetrofit
import com.example.musicsharing.retrofit.api.WebApi
import com.example.musicsharing.ui.theme.MusicSharingTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import profileScreen

class NavigationActivity : ComponentActivity() {
    private val webApi = WebRetrofit().getInstance().create(WebApi::class.java)
    private val accountsRetrofit = AccountsRetrofit()
    private lateinit var sharedPreferences: SharedPreferences
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val spotifyId = sharedPreferences.getString("spotifyId", "")
        val userName = sharedPreferences.getString("userName", "")

        setContent {
            MusicSharingTheme {
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = "Login"
                ) {
                    composable("Login") {
                        LoginScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController : NavHostController = rememberNavController()

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry = navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry.value?.destination

                    listOfNavItems.forEach { navItem ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                            onClick = {
                                navController.navigate(navItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true

                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(text = navItem.label)
                            } ,
                        )
                    }
                }
            }

        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screens.PostsScreen.name,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = Screens.PostsScreen.name) {
                    //FeedScreen()
                }
                composable(route = Screens.ProfileScreen.name) {
                    //profileScreen()
                }
                composable(route = Screens.GreetingsScreen.name) {
                    //GreetingsScreen()
                }
                composable(route = Screens.FriendsScreen.name) {
                    //FriendsScreen()
                }
            }
        }
    }

    private fun signOut(){
        sharedPreferences.edit().putBoolean(SharedPreferencesConstants.KEY_LOGGED_IN, false).apply()
        sharedPreferences.edit().remove(SharedPreferencesConstants.KEY_SPOTIFY_ID).apply()
        sharedPreferences.edit().remove(SharedPreferencesConstants.KEY_USER_ID).apply()
        sharedPreferences.edit().remove(SharedPreferencesConstants.KEY_TOKEN).apply()
        //startActivity(Intent(this, LoginActivity::class.java))
    }

    private suspend fun getSongsList(input: String): List<Track> {
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
    }
}