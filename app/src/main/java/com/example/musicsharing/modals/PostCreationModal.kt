package com.example.musicsharing.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.example.musicsharing.classes.PostPayload
import com.example.musicsharing.classes.Track

@Composable
fun PostCreationDialog(setShowDialog: (Boolean) -> Unit,getSongsList: suspend (String) -> List<Track>, createPostInfo: (PostPayload) -> Unit ) {
    var song by remember { mutableStateOf("")}
    var caption by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Dialog(onDismissRequest = { setShowDialog(false) }, properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .fillMaxWidth()
                .height(500.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

            ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))

            ) {

                Text(
                    text = "Create Post",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = "Search for song",
                    style = MaterialTheme.typography.bodyLarge
                )

                SongDropdownSearch(getSongsList) {
                    returnedSong ->
                    song = returnedSong
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = "Caption",
                    style = MaterialTheme.typography.bodyLarge
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = { Text(text = "Add Caption") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {keyboardController?.hide()})
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF309CA9)),
                        onClick = {
                            setShowDialog(false)
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(35.dp)
                            .width(100.dp)

                    ) {
                        Text(text = "Cancel")

                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF309CA9)),
                        onClick = {
                            setShowDialog(false)
                            var post = PostPayload("",caption,"","","",song,0 )
                            createPostInfo(post)
                        },
                        modifier = Modifier
                            .height(35.dp)
                            .width(100.dp)

                    ) {
                        Text(text = "Post")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDropdownSearch(getSongsList: suspend (String) -> List<Track>, selectedTrack:(String) -> Unit) {
    var tracks: List<Track>
    var options by remember { mutableStateOf<List<String>>(emptyList()) }
    var song by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    LaunchedEffect(song) {
       if (song != "") {
            tracks = getSongsList(song)
            options = tracks.map { it.name }
        }
        else {
            options = emptyList()
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = selectedOptionText.takeIf { it.isNotEmpty() } ?: song,
            onValueChange = { song = it },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
        )

        if (options.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier
                    .background(Color.White)
                    .exposedDropdownSize(true)
                ,
                properties = PopupProperties(focusable = false),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                            selectedTrack(selectedOptionText)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewPostCreationModal() {
    PostCreationDialog()
}*/