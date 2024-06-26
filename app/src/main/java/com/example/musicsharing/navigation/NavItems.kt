package com.example.musicsharing.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItems(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val listOfNavItems = listOf(
    NavItems(
        label = "Posts",
        icon = Icons.Default.Add,
        route = Screens.PostsScreen.name
    ),
    NavItems(
        label ="Profile",
        icon = Icons.Default.Person,
        route = Screens.ProfileScreen.name
    ),
    /*NavItems(
        label ="Add Friends",
        icon = Icons.Default.Add,
        route = Screens.FriendsScreen.name
    )*/
)
