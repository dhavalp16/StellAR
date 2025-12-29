package com.cosmic_struck.stellar.stellar.home.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani

@Composable
fun BottomCaptions(
    captions:String,
    modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = captions,
            textAlign = TextAlign.Justify,
            color = Color.White,
            fontFamily = Rajdhani,
            fontWeight = FontWeight.Normal, // or FontWeight.W700
            fontSize = 20.sp
        )
    }

}