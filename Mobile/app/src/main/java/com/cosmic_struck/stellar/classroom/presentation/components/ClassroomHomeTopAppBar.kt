package com.cosmic_struck.stellar.classroom.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani

@Composable
fun ClassroomTopAppBar(
    classroomName: String = "Lalit",
    classroomAuthor: String = "21",
    classroomMembers: String = "21",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(bottom = 20.dp, start = 20.dp)// Added padding for better spacing
    ) {// Text Column (Left Side)
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = classroomName, // Added "Hello, " for friendlier UI
                    fontFamily = Rajdhani,
                    fontSize = 28.sp, // Slightly larger for header impact
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = classroomAuthor, // Added context to the number
                    fontFamily = Rajdhani,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = classroomMembers,
                    fontFamily = Rajdhani,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
        }
    }
}
