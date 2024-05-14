package com.example.musicsharing.displayScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.musicsharing.models.Post
import com.example.musicsharing.navigation.Screens
import com.example.musicsharing.navigation.listOfNavItems

@Composable
fun PostsScreen(onNavigateToRoute: (String) -> Unit) {
    var posts = remember { mutableStateListOf<Post>() }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val data = posts
        posts.addAll(data)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                listOfNavItems.forEach { navItem ->
                    NavigationBarItem(
                        selected = navItem.route == Screens.PostsScreen.name,
                        onClick = { onNavigateToRoute(navItem.route) },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ){paddingValues -> Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFF3E8))
            .padding(paddingValues)

    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(posts) { index, post ->
                PostItem(postContent = post)
            }
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }}

        /*if (showDialog) {
            PostCreationDialog(setShowDialog = {
                showDialog = it
            }, getSongsList) {
                post ->
                if (post != null){
                    CoroutineScope(Dispatchers.IO).launch {
                        posts.add(0,sendPostInfo(post))
                    }
                }
            }
        }*/
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SocialMediaPostScreen()
}

*/