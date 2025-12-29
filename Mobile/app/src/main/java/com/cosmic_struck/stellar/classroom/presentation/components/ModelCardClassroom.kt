package com.cosmic_struck.stellar.classroom.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun ModelCardClassroom(
    navigateToModelScreen: () -> Unit,
    modelURL: String,
    modelThumbnail: String,
    modelName: String,
    modelDescription: String,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(24.dp), ambientColor = Color.DarkGray)
            .clickable{
                navigateToModelScreen()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                imageModel = {modelThumbnail},
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
            )
            Column(

            ) {
                Text(
                    text = modelName,
                    fontFamily = Rajdhani,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = modelDescription,
                    fontFamily = Rajdhani,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

        }
    }
}