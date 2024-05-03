package com.example.musicsharing.navigation

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
import com.example.musicsharing.classes.Post
import com.example.musicsharing.classes.Track
import com.example.musicsharing.classes.PostPayload
import com.example.musicsharing.displayScreens.GreetingsScreen
import com.example.musicsharing.displayScreens.SocialMediaPostScreen
import com.example.musicsharing.displayScreens.FriendsScreen
import profileScreen

@Composable
fun AppNavigation(
    signOut: () -> Unit,
    addFriend: (String) -> Unit,
    getPostFeed: suspend () -> List<Post>,
    sendPostInfo: suspend (PostPayload) -> Post,
    getSongsList: suspend (String) -> List<Track>,
    spotifyId: String?,
    userName: String?
) {
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
                modifier = Modifier
                    .padding(paddingValues)

            ) {
                composable(route = Screens.PostsScreen.name) {
                    SocialMediaPostScreen(getPostFeed, sendPostInfo, getSongsList)
                }
                composable(route = Screens.ProfileScreen.name) {
                    profileScreen(signOut, spotifyId, userName)
                }
                composable(route = Screens.GreetingsScreen.name) {
                    GreetingsScreen()
                }
                composable(route = Screens.FriendsScreen.name) {
                    FriendsScreen(addFriend)
                }
            }
        }
}