package com.example.musicsharing.modals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogoutDialog(signOut: () -> Unit) {
    var showDialog by remember { mutableStateOf(true) } // Control the visibility of the dialog

    if (showDialog) {
        val navController = rememberNavController()
        Dialog(onDismissRequest = { showDialog = false }) { // Close the dialog when the user clicks outside of it
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .fillMaxWidth()// Makes the card take up the full width of its parent
                    .height(150.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

                ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .height(150.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF309CA9))
                            .padding(bottom = 8.dp),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Are You Sure You Want To Log Out?",
                            fontSize = 17.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 17.dp, start = 15.dp, bottom = 5.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .drawWithContent {
                                drawContent() // Draw the original content of the Row
                                drawLine(
                                    color = (Color(0xFF00889A)), // Set the color of the border
                                    strokeWidth = 1.dp.toPx(), // Set the thickness of the border
                                    start = Offset(x = 0f, y = 0f), // Start from the top left corner
                                    end = Offset(x = size.width, y = 0f) // Draw to the top right corner
                                )
                            },

                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Color(0xFF309CA9)),
                            onClick = {
                                signOut()
                                showDialog = false
                                      },
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 10.dp, top = 30.dp, end = 10.dp)
                                .height(40.dp)
                                .width(100.dp)
                        ) {
                            Text(text = "Yes")
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(Color(0xFF309CA9)),
                            onClick = {
                                showDialog = false
                                      },
                            modifier = Modifier
                                .padding(start = 50.dp, bottom = 10.dp, top = 10.dp, end = 10.dp)
                                .height(40.dp)
                                .width(100.dp)

                        ) {
                            Text(text = "No")
                        }
                    }
                }
            }
        }
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewLogoutModal() {
    LogoutDialog()
}*/
