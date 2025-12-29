package com.cosmic_struck.stellar.stellar.models.presentation.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.classroom.data.dto.ClassroomModel
import com.cosmic_struck.stellar.common.util.Rajdhani

val rare = Color(0xFFAD46FF)
val common = Color(0xFFC80000)
val secondary = Color(0xFFF0B100)

@Composable
fun PlanetCard(
    locked: Boolean,
    navigateToModelViewer: (String, String) -> Unit,
    planet: ClassroomModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 5.dp)
            .alpha(if (locked) 0.6f else 1f),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 1f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Left side - Planet Image
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.4f)
                        .clip(shape = RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp))
                        .background(color = Color.Black)
                ) {
                    CoilImage(
                        imageModel = { planet.model_thumbnail },
                        imageOptions = ImageOptions(contentScale = ContentScale.FillBounds),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Right side - Info and Buttons
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top section - Title, Rarity, XP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = planet.model_name,
                                color = Color.Black,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                fontFamily = Rajdhani,
                                fontSize = 18.sp
                            )
                            RarityBadge(rarity = planet.rarity)
                        }

                        Text(
                            text = "${planet.xp_reward} XP",
                            color = Color(0xffad46ff),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            fontFamily = Rajdhani,
                            fontSize = 16.sp
                        )

                        // Middle section - Description
                        Text(
                            text = planet.description ?: "Description",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.padding(bottom = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            ActionButton(
                                icon = R.drawable.eye,
                                tintColor = Color(0xff0061fc),
                                backgroundColor = Color.White,
                                modifier = Modifier
                                    .size(45.dp)
                                    .border(
                                        border = BorderStroke(2.dp, Color(0xff0061fc)),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                onClick = {
                                    if (!locked) {
                                        navigateToModelViewer(
                                            planet.model_url,
                                            planet.model_name
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "You Haven't Unlocked This Yet!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )

                            ActionButton(
                                icon = R.drawable.thunder,
                                tintColor = Color.White,
                                backgroundColor = Color(0xffad46ff),
                                modifier = Modifier.size(45.dp),
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // Locked Overlay
            if (locked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_block_24),
                            contentDescription = "Locked",
                            tint = Color.White,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(bottom = 8.dp)
                        )

                        Text(
                            text = "LOCKED",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            fontFamily = Rajdhani,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Text(
                            text = "Unlock by completing\nmore missions",
                            color = Color.White.copy(alpha = 0.8f),
                            style = TextStyle(
                                fontSize = 12.sp,
                                letterSpacing = 0.5.sp
                            ),
                            fontFamily = Rajdhani,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RarityBadge(rarity: String) {
    val rarityColor = when {
        rarity.equals("Rare", ignoreCase = true) -> rare
        rarity.equals("Common", ignoreCase = true) -> common
        else -> secondary
    }

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = rarityColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rarity,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            fontFamily = Rajdhani
        )
    }
}

@Composable
private fun ActionButton(
    icon: Int,
    tintColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = backgroundColor),
        content = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Action",
                tint = tintColor,
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    )
}