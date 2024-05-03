package com.example.musicsharing.displayScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp

@Composable
fun FriendsScreen(addFriend:  (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFF3E8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var userName by rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF309CA9))
                .size(800.dp)
        ) {
            Text(
                text = "Add Friends",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFFBFFDC),
                fontSize = 38.sp,
                modifier = Modifier
                    .padding(top = 25.dp)
                    .align(Alignment.TopCenter)
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(350.dp)
                    .padding(start = 20.dp, end = 20.dp, top = 5.dp)
                    .padding(vertical = 70.dp),
                thickness = 1.dp,
                color = Color.White
            )

            Button(
                onClick = {
                    addFriend(userName)
                    userName = ""
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .width(1000.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFFBFFDC))
            ) {
                Text(
                    text = "Add New Friend",
                    color = Color(0xFF00889A),
                    fontSize = 18.sp,
                )
            }

            TextField(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 90.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = userName,
                onValueChange = { userName = it },
                placeholder = { Text(text = "e.g. NoahGarfunkel") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()})
            )
        }
    }
}

