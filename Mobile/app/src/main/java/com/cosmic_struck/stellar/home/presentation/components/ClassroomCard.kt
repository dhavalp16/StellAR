package com.cosmic_struck.stellar.home.presentation.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.common.util.getClassroomColor
import com.cosmic_struck.stellar.home.data.dto.JoinedClassroom

@Composable
fun ClassroomCard(
    onClick: () -> Unit,
    classroom: JoinedClassroom,
    modifier: Modifier = Modifier) {

    val color = getClassroomColor(
        classroomId = classroom.classroom_id
    )
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Card(
        modifier = modifier,
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = Color.Black
        ),
        border = BorderStroke(
            width = if (classroom.is_creator) 3.dp else 1.dp,
            color = if (classroom.is_creator) Color.Green else Color.Black
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = classroom.classroom_name,
                fontFamily = Rajdhani,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =  classroom.classroom_name,
                fontFamily = Rajdhani,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "No of Members: ${classroom.member_count}",
                fontFamily = Rajdhani,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal

            )
            Text(
                    text = "Join Code: ${classroom.join_code}",
                    fontFamily = Rajdhani,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(classroom.join_code))
                        Toast
                            .makeText(context, "Join code copied", Toast.LENGTH_SHORT)
                            .show()
                    }
            )
        }
    }
}