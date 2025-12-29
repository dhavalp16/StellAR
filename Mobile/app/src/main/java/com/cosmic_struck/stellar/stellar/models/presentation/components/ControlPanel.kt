package com.cosmic_struck.stellar.stellar.models.presentation.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.stellar.models.presentation.viewmodel.SceneType

@Composable
fun ControlPanel(
    scene : SceneType,
    onToggleScene: () -> Unit,
    rotationSpeed: Float,
    zoomScale: Float,
    cameraDistance: Float,
    onRotationSpeedChange: (Float) -> Unit,
    onZoomScaleChange: (Float) -> Unit,
    onCameraDistanceChange: (Float) -> Unit,
    modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rotation Control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Rotation",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        "Speed",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_media_play),
                        contentDescription = "Play",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                    Slider(
                        value = rotationSpeed,
                        onValueChange = { onRotationSpeedChange(it) },
                        modifier = Modifier.weight(1f),
                        valueRange = 0f..10f,
                        steps = 10
                    )
                }

                Text(
                    "1x",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Zoom Control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Zoom",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        "Scale",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Slider(
                        value = zoomScale,
                        onValueChange = { onZoomScaleChange(it) },
                        modifier = Modifier.weight(1f),
                        valueRange = 0.5f..50.0f,
                        steps = 15
                    )
                }

                Text(
                    String.format("%.1f", zoomScale) + "x",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onToggleScene() },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C3AED)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (scene== SceneType.SceneView)"View in AR" else "View in Scene",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = {
                        onRotationSpeedChange(0.5f)
                        onZoomScaleChange(1.0f)
                        onCameraDistanceChange(3.0f)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Reset View",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
