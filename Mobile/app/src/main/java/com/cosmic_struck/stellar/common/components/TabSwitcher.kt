package com.cosmic_struck.stellar.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani

@Composable
fun TabSwitcher(
    nonActiveTextColor: Color = Color.Black,
    activeTextColor: Color = Color.White,
    modifier: Modifier = Modifier,
    options: List<String> = listOf("My Collection", "Discover"),
    initialIndex: Int = 0,
    onOptionSelected: (Int) -> Unit = {},
    fontFamily: FontFamily = Rajdhani // Replace with Rajdhani if available
) {
    var selectedIndex by remember { mutableStateOf(initialIndex) }

    // Colors derived from the visual style
    val activePurple = Color(0xFF9700FF) // Bright purple for selected state
    val containerPurple = Color(0xFF2A0055).copy(alpha = 0.4f) // Dark background
    val borderPurple = Color(0xFF552288).copy(alpha = 0.5f) // Subtle border

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderPurple, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .padding(4.dp) // Padding between container edge and buttons
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, text ->
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) activePurple else Color.Transparent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null // Removes default ripple for cleaner look
                        ) {
                            selectedIndex = index
                            onOptionSelected(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = if (isSelected) activeTextColor else nonActiveTextColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }
            }
        }
    }
}