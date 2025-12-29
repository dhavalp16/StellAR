package com.cosmic_struck.stellar.auth.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.auth.presentation.viewmodel.AuthViewModel
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.ui.theme.Blue5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAccountScreen(
    navigateback: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier) {
    val email = remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    val check1 = remember { mutableStateOf(false) }
    val check2 = remember { mutableStateOf(false) }
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
    val showError = remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(state.success) {
        if(state.success){
            navigateToHomeScreen()
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Login into account",
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateback()
                        }
                    ){
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {it->
        Box(
            modifier = Modifier
                .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),

            ) {

                Text(
                    text = "Email",
                    modifier = Modifier
                        .padding(start = 16.dp),
                    textAlign = TextAlign.Start,
                    fontFamily = Rajdhani,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        if (email.value.matches(emailPattern.toRegex()))
                            check1.value = true
                        else
                            check1.value = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(24.dp)),
                    placeholder = {
                        Text(
                            text = "example@example",
                            fontFamily = Rajdhani,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Password",
                    modifier = Modifier
                        .padding(start = 16.dp),
                    textAlign = TextAlign.Start,
                    fontFamily = Rajdhani,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (password.length >= 8)
                            check2.value = true
                        else
                            check2.value = false},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(24.dp)),
                    placeholder = { Text("abc123@#$", fontWeight = FontWeight.Light) },
                    visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (isVisible) R.drawable.eye else R.drawable.eye
                        IconButton(onClick = { isVisible = !isVisible }) {
                            Icon(painter = painterResource(id = icon), contentDescription = "Toggle Visibility")
                        }
                    },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(20.dp))
                LaunchedEffect(state.error) {
                    if(state.error.isNotEmpty()){
                        showError.value = true
                    }
                }
                if(showError.value){
                    Text(
                        text = state.error,
                        color = Color.Red,
                        fontFamily = Rajdhani,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Button(
                    onClick = {
                        viewModel.setEmailAddress(email.value)
                        viewModel.setPassword(password)
                        viewModel.signInWithEmail()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(24.dp),
                    enabled = check1.value && check2.value,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue5,
                        contentColor = Color.White,
                        disabledContainerColor = Blue5.copy(alpha = 0.6f),
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Continue", fontFamily = Rajdhani, fontSize = 16.sp)
                }

            }
        }
    }
}