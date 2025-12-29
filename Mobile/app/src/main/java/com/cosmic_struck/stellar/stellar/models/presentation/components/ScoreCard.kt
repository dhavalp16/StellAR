package com.cosmic_struck.stellar.stellar.models.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.ui.theme.Blue5

@Composable
fun ScoreCard(
    icon: @Composable ()-> Unit,
    title: String,
    score: String,
    modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .height(175.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Blue5.copy(0.6f)
        ),
        colors = cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically),
        ) {
            icon()
            Text(
                text = score,
                color = Color.White,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = title,
                color = Color.White,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}