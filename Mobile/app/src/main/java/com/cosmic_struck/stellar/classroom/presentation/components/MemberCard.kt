package com.cosmic_struck.stellar.classroom.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun MemberCard(
    imageUrl: String = "https://imgs.search.brave.com/1YAkwFJgJuiuf-udYRtZZ2Fgi91naIYMLrK9Rj9PA3Y/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pbWcu/ZnJlZXBpay5jb20v/cHJlbWl1bS1waG90/by9yZWFsaXN0aWMt/c2F0dXJuLWlzb2xh/dGVkLWJsYWNrLWJh/Y2tncm91bmQtM2Qt/cmVuZGVyaW5nLXdp/dGhvdXQtYWktZ2Vu/ZXJhdGVkXzE2OTk2/My0yMzkyLmpwZz9z/ZW10PWFpc19oeWJy/aWQmdz03NDAmcT04/MA",
    name: String = "",
    modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(24.dp), ambientColor = Color.DarkGray),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                imageModel = {imageUrl},
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
            )

            Text(
                text = name,
                fontFamily = Rajdhani,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}