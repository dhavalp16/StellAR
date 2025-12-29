package com.cosmic_struck.stellar.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.navigation.Screens
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.ui.theme.BackgroundPrimary
import com.cosmic_struck.stellar.ui.theme.Blue4
import com.cosmic_struck.stellar.ui.theme.Blue5
import com.cosmic_struck.stellar.ui.theme.DarkBlue2
import com.cosmic_struck.stellar.ui.theme.Grey1

data class BottomAppBarItems(
    val title: String,
    val route: String,
    @DrawableRes val image: Int
)

val bottomAppBarItems = listOf(
    BottomAppBarItems(
        title = "Home",
        route = Screens.StellarHomeScreen.route,
        image = R.drawable.vector
    ),
    BottomAppBarItems(
        title = "Models",
        route = "model_graph",
        image = R.drawable.db
    ),
    BottomAppBarItems(
        title = "AR Lab",
        route = Screens.ARLabScreen.route,
        image = R.drawable.beaker
    )
)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomAppBar(navController: NavController){
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(shape = RoundedCornerShape(50.dp)),
        containerColor = BackgroundPrimary.copy(alpha = 0.2f),
        tonalElevation = 10.dp
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route
        bottomAppBarItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.image),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                },
                label = {
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
                            fontFamily = Rajdhani,
                            fontWeight = FontWeight(700),
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp,
                        )
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Blue5,
                    selectedTextColor = Blue5,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = Grey1,
                    unselectedTextColor = Grey1,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent
                )
            )
        }
    }
}