package com.example.musicsharing.displayScreens

import androidx.compose.runtime.*

@Composable
fun PostsScreen() {
    /*var posts = remember { mutableStateListOf<Post>() }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val data = getPostFeed()
        posts.addAll(data)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFF3E8))

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
        }
        if (showDialog) {
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
        }
    }*/
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SocialMediaPostScreen()
}

*/