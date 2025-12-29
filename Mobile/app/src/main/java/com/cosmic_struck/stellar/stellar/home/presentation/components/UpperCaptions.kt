package com.cosmic_struck.stellar.stellar.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.HomeScreenCaptions
import com.cosmic_struck.stellar.common.util.Rajdhani

@Composable
fun UpperCaptions(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🚀 StellAR",
                color = Color.White,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold, // or FontWeight.W700
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = HomeScreenCaptions,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                color = Color.White,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Normal, // or FontWeight.W700
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 16.sp,
                    maxFontSize = 24.sp,
                    stepSize = 1.sp
                )
            )
        }
    }

}