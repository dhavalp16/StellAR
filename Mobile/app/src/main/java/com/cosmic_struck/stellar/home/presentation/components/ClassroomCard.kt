package com.cosmic_struck.stellar.home.presentation.components

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

    val color = getClassroomColor()
    Card(
        modifier = modifier,
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = Color.Black
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
        }
    }
}