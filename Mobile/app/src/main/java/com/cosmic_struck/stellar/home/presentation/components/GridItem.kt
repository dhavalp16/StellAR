package com.cosmic_struck.stellar.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani

@Composable
fun GridItem(
    onClick: () -> Unit,
    color: Color,
    title: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .size(150.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold
            )

            Icon(
                painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
            )
        }
    }

}