import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import com.example.musicsharing.R
import com.example.musicsharing.modals.LogoutDialog


@Composable
fun profileScreen(signOut: () -> Unit, spotifyId: String?, userName: String?) {

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
          .background(color = Color(0xFFFFF3E8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //EDIT BUTTON
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End

        ) {
            IconButton(
                onClick = {
                          /* ADD EDIT FUNCTIONALITY HERE */
                          },
                modifier = Modifier
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint =  Color(0xFF309CA9)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.apa_profile_picture), //make dynamic
            contentDescription = "Profile Picture",
            modifier = Modifier
                .padding(top = 5.dp)
                .size(200.dp)
                .shadow(5.dp, CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF309CA9))
                .size(800.dp)
        )
        {
            if (userName != null) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFFFBFFDC),
                    fontSize = 38.sp,
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .align(Alignment.TopCenter)

                )
            }



            HorizontalDivider(
                modifier = Modifier
                    .width(350.dp)
                    .padding(start = 20.dp,end = 20.dp, top = 5.dp)
                    .padding(vertical = 70.dp),
                thickness = 1.dp,
                color = Color.White
            )

            if (spotifyId != null){
                Text(
                    text = spotifyId,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 19.sp,
                    color = Color(0xFFFBFFDC),
                    modifier = Modifier
                        .padding(top = 85.dp)
                        .align(Alignment.TopCenter)

                )
            }

            Button(
                onClick = {
                    showDialog = true
                          },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .width(1000.dp)
                    .height(50.dp),

                colors = ButtonDefaults.buttonColors(Color(0xFFFBFFDC)) //

            ) {
                Text(
                    text = "Logout",
                    color = Color(0xFF00889A),
                    fontSize = 18.sp,

                )


            }
            if (showDialog) {
                LogoutDialog(signOut)
            }

            }

        }

    }

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    profileScreen()
}*/
