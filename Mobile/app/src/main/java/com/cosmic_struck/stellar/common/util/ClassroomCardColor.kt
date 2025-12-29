package com.cosmic_struck.stellar.common.util

import android.util.Log
import androidx.compose.ui.graphics.Color

fun getClassroomColor() : Color{
    val colors = listOf<Color>(
        Color(0xFFFF4081),
        Color(0xFF00B0FF),
        Color(0xFF00E676),
        Color(0xFFFFC400)
    )

    val randIndex = ((Math.random() * 10) % 3).toInt()
    Log.d("Random Index Checking","$randIndex")
    return colors[randIndex]
}