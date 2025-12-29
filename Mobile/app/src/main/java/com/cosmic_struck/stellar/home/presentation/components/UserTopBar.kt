package com.cosmic_struck.stellar.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun UserTopBar(
    userName: String = "Lalit",
    userLevel: String = "21",
    userPic: String = "https://imgs.search.brave.com/i8w7QWmSRvY3mC70TNOOunvd8yPkGXNaD96UI-8G_Jc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdGF0/aWMudmVjdGVlenku/Y29tL3N5c3RlbS9y/ZXNvdXJjZXMvdGh1/bWJuYWlscy8wNTYv/MzkxLzg1NC9zbWFs/bC9wcm9maWxlLXZp/ZXctb2YtYS15b3Vu/Zy1tYW4taW4tbWlu/aW1hbC1pbGx1c3Ry/YXRpb24tc3R5bGUt/YXJ0LXZlY3Rvci5q/cGc",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 10.dp)
            .padding(bottom = 20.dp)// Added padding for better spacing
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Pushes text to left, image to right
        ) {
            // Text Column (Left Side)
            Column(
                modifier = Modifier.weight(1f) // Takes up remaining space but leaves room for image
            ) {
                Text(
                    text = "Hello, $userName", // Added "Hello, " for friendlier UI
                    fontFamily = Rajdhani,
                    fontSize = 28.sp, // Slightly larger for header impact
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Level $userLevel Explorer", // Added context to the number
                    fontFamily = Rajdhani,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            // User Image (Right Side)
            CoilImage(
                imageModel = { userPic },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .size(70.dp) // Fixed size for the profile picture
                    .clip(CircleShape)
                    .background(color = Color.Gray)
                    .border(
                        width = 2.dp,
                        color = Color(0xFFD0BCFF), // Light purple border to match theme
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview
@Composable
fun UserTopBarPreview(modifier: Modifier = Modifier) {
    UserTopBar()
}