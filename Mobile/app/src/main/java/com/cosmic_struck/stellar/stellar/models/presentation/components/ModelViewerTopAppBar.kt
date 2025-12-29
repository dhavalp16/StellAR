package com.cosmic_struck.stellar.stellar.models.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.util.Rajdhani

@Composable
fun ModelViewerTopAppBar(
    onNavigateBack: () -> Unit,
    name: String,
    modifier: Modifier = Modifier) {

    // Top Bar
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { onNavigateBack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(painterResource(id = R.drawable.cross), contentDescription = "Close", tint = Color.White)
        }
            Text(
                name,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center),
                fontFamily = Rajdhani,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )

        IconButton(
            onClick = { /* Handle info */ },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(painterResource(id = R.drawable.ci_info), contentDescription = "Info", tint = Color.White)
        }
    }
}