package com.cosmic_struck.stellar.stellar.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.ui.theme.Blue6
import com.cosmic_struck.stellar.ui.theme.ButtonPrimary

@Composable
fun ScanButton(
    navigateToScanText: () -> Unit,
    modifier: Modifier = Modifier) {
    Button(
        onClick = {
            navigateToScanText()
        },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        ButtonPrimary,
                        Blue6
                    )
                )
            ),
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(R.drawable.scan),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Scan Textbook Pages",
                color = Color.White,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold, // or FontWeight.W700
            )
        }
    }
}