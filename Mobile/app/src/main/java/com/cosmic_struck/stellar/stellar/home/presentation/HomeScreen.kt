package com.cosmic_struck.stellar.stellar.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.BottomAppBar
import com.cosmic_struck.stellar.common.util.HomeScreenCaptions2
import com.cosmic_struck.stellar.stellar.home.presentation.components.BottomCaptions
import com.cosmic_struck.stellar.stellar.home.presentation.components.ScanButton
import com.cosmic_struck.stellar.stellar.home.presentation.components.UpperCaptions

@Composable
fun StellarHomeScreen(
    navHostController: NavHostController,
    navigateToScanText: () -> Unit,
    viewModel: StellarHomeScreenViewModel = hiltViewModel<StellarHomeScreenViewModel>(),
    modifier: Modifier = Modifier) {
    BackgroundScaffold(
        bottomBar = {
            BottomAppBar(navHostController)
        }
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {

                UpperCaptions()
                Spacer(modifier = Modifier.height(50.dp))
                ScanButton(
                    navigateToScanText = navigateToScanText
                )
                Spacer(modifier = Modifier.height(50.dp))
                BottomCaptions(HomeScreenCaptions2)
            }
        }
    }

}