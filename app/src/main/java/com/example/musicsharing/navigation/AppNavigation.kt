package com.example.musicsharing.navigation

import ProfileScreen
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.musicsharing.displayScreens.AccountCreationScreen
import com.example.musicsharing.displayScreens.LoginScreen
import com.example.musicsharing.displayScreens.PostsScreen
import com.example.musicsharing.sharedPreferences.SharedPreferencesApi

@Composable
fun AppNavigation(
    uri: Uri?,
    navController: NavHostController = rememberNavController()
    ) {
    var startDestination = "LoginNavigation"
    if (SharedPreferencesApi.getIsLoggedIn()) {
        startDestination = "PostsScreen"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screens.PostsScreen.name) {
            PostsScreen(onNavigateToRoute = { route -> navController.navigate(route) })
        }
        composable(route = Screens.ProfileScreen.name) {
            ProfileScreen(onNavigateToRoute = { route -> navController.navigate(route) })
        }
        composable(route = Screens.FriendsScreen.name) {
            //FriendsScreen()
        }
        navigation(startDestination = "Login", route = "LoginNavigation") {
            composable("Login") {
                LoginScreen()
                if (uri != null && uri.scheme == "music-sharing" && uri.host == "redirect" && uri.path == "/main-activity/account-creation") {
                    val code = uri.getQueryParameter("code")?.let { "$it" }
                    if (code != null){
                        navController.navigate("AccountCreation/${code}")
                    }
                }
            }
            composable("AccountCreation/{code}") { backStackEntry ->
                AccountCreationScreen(
                    onNavigateToPosts = { navController.navigate("PostsScreen") },
                    backStackEntry.arguments?.getString("code")
                )
            }
        }
    }
}