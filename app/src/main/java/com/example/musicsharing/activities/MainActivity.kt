package com.example.musicsharing.activities

import android.content.Context
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
import androidx.navigation.navigation
import com.example.musicsharing.classes.Track
import com.example.musicsharing.constants.SharedPreferencesConstants
import com.example.musicsharing.displayScreens.AccountCreationScreen
import com.example.musicsharing.displayScreens.LoginScreen
import com.example.musicsharing.displayScreens.PostsScreen
import com.example.musicsharing.navigation.Screens
import com.example.musicsharing.navigation.listOfNavItems
import com.example.musicsharing.retrofit.AccountsRetrofit
import com.example.musicsharing.retrofit.WebRetrofit
import com.example.musicsharing.retrofit.api.WebApi
import com.example.musicsharing.ui.theme.MusicSharingTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private val webApi = WebRetrofit().getInstance().create(WebApi::class.java)
    private val accountsRetrofit = AccountsRetrofit()
    private lateinit var sharedPreferences: SharedPreferences
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        setContent {
            MusicSharingTheme {
                MainAppNavigation()
            }
        }
    }

    @Composable
    fun MainAppNavigation() {
        val navController = rememberNavController()

        var startScreen = "LoginNavigation"
        if (sharedPreferences.getBoolean("KEY_LOGGED_IN", false)){
            startScreen = "PostsScreen"
        }

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
                            },
                        )
                    }
                }
            }

        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startScreen,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = Screens.PostsScreen.name) {
                    PostsScreen()
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
                navigation(startDestination = "Login", route = "LoginNavigation") {
                    composable("Login") {
                        LoginScreen()
                        val uri = intent.data
                        if (uri != null && uri.scheme == "music-sharing" && uri.host == "redirect") {
                            val path = uri.path
                            if (path == "/main-activity/account-creation") {
                                val code = uri.getQueryParameter("code")
                                navController.navigate("AccountCreation/${code}")
                            }
                        }
                    }
                    composable("AccountCreation/{code}") { backStackEntry ->
                        AccountCreationScreen(
                            onNavigateToAppNavigation = { navController.navigate("PostsScreen") },
                            sharedPreferences,
                            backStackEntry.arguments?.getString("code")
                        )
                    }
                    /*composable("AppNavigation") {
                        MainAppNavigation()
                    }*/
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