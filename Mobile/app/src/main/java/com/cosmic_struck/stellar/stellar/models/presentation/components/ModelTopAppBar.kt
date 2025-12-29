package com.cosmic_struck.stellar.stellar.models.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.util.Rajdhani

// Define colors to match the image
val CardGradientStart = Color(0xFF120029) // Very dark purple
val CardGradientEnd = Color(0xFF3D0075)   // Lighter purple
val GoldAccent = Color(0xFFFFD700)
val XPAccent = Color(0xFFD0BCFF)

@Composable
fun ModelTopAppBar(
    modifier: Modifier = Modifier,
    currentLevel: Int = 5,
    currentXP: Int = 1250,
    maxXP: Int = 1500,
    fontFamily: FontFamily = Rajdhani // Replace with Rajdhani if available
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            Column{
                // Top Row: Title/Subtitle vs Level/XP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Side: Titles
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Space Models",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamily
                        )
                        Text(
                            text = "Collect and explore the universe",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamily
                        )
                    }

                    // Right Side: Stats
                    Column(horizontalAlignment = Alignment.End) {
                        // Level Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.trophy), // Trophy Icon
                                contentDescription = "Level",
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Level $currentLevel",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = fontFamily
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // XP Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.spark), // Sparkles Icon
                                contentDescription = "XP",
                                tint = XPAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$currentXP XP",
                                color = XPAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = fontFamily
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom Row: Progress Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progress to Level ${currentLevel + 1}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontFamily = fontFamily
                    )
                    Text(
                        text = "$currentXP/$maxXP XP",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { currentXP.toFloat() / maxXP.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF7C4DFF), // Bright purple for progress
                    trackColor = Color(0xFF0D001A), // Very dark background for track
                    strokeCap = StrokeCap.Round,
                )
            }
        }
    }
}