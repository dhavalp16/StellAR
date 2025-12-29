package com.cosmic_struck.stellar.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cosmic_struck.stellar.common.navigation.Screens
import com.cosmic_struck.stellar.auth.presentation.components.LowerPortion
import com.cosmic_struck.stellar.auth.presentation.components.UpperPortion
import com.cosmic_struck.stellar.auth.presentation.viewmodel.AuthViewModel
import com.cosmic_struck.stellar.common.components.BackgroundScaffold

@Composable
fun AuthScreen(
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToHomeScreen : () -> Unit,
    viewmodel: AuthViewModel = hiltViewModel<AuthViewModel>(),
    modifier: Modifier = Modifier) {

    val state = viewmodel.state.value

    LaunchedEffect(state.success) {
        if(state.success){
            navigateToHomeScreen()
        }
    }
    BackgroundScaffold(

    ) {
        Box(
            modifier = it
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier
                    .weight(1f))
                UpperPortion(
                    modifier = Modifier
                        .weight(1f)
                )
                LowerPortion(

                    modifier = Modifier
                        .weight(1f),
                    navigateToLoginAccount = navigateToLoginScreen,
                    navigateToCreateAccount = navigateToSignUpScreen
                )

            }
        }
    }


}