package com.cosmic_struck.stellar.biology.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cosmic_struck.stellar.common.navigation.Screens

@Composable
fun BiologyHomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Biology Home Screen"
        )
    }
}