package com.example.musicsharing.navigation

import ProfileScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.musicsharing.displayScreens.AccountCreationScreen
import com.example.musicsharing.displayScreens.LoginScreen
import com.example.musicsharing.displayScreens.PostsScreen

@Composable
fun MainAppNavigation(startScreen: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startScreen,) {
        composable(route = Screens.PostsScreen.name) {
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry.value?.destination
            PostsScreen(
                currentDestination,
                navigationFunction = { route -> navController.navigate(route) })
        }
        composable(route = Screens.ProfileScreen.name) {
            ProfileScreen(navigationFunction = { route -> navController.navigate(route) })
        }
        composable(route = Screens.FriendsScreen.name) {
            //FriendsScreen()
        }
        navigation(startDestination = "Login", route = "LoginNavigation") {
            composable("Login") {
                LoginScreen()
            }
            composable("AccountCreation/{code}") { backStackEntry ->
                AccountCreationScreen(
                    onNavigateToAppNavigation = { navController.navigate("PostsScreen") },
                    backStackEntry.arguments?.getString("code")
                )
            }
        }
    }
}