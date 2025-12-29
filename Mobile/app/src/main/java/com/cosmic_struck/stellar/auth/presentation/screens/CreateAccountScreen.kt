package com.cosmic_struck.stellar.auth.presentation.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.Alignment
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
fun CreateAccountScreenEmailValidation(
    navigateback : () -> Unit = {},
    navigateToPasswordValidation: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Add Your Email",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {navigateback()}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {it->

        val email = remember { mutableStateOf("") }
        val activeButton = remember { mutableStateOf(false) }
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
        var username by remember { mutableStateOf("") }



        Box(
            modifier = Modifier
                .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "User Name",
                    modifier = Modifier
                        .padding(start = 16.dp),
                    textAlign = TextAlign.Start,
                    fontFamily = Rajdhani,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(24.dp)),
                    placeholder = {
                        Text(
                            text = "Abc",
                            fontFamily = Rajdhani,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
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
                            activeButton.value = true
                        else
                            activeButton.value = false
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

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.setEmailAddress(
                            email.value
                        )
                        viewModel.setUsername(
                            username
                        )
                        navigateToPasswordValidation()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(24.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue5,
                        contentColor = Color.White,
                        disabledContainerColor = Blue5.copy(alpha = 0.6f),
                        disabledContentColor = Color.White.copy(0.6f)
                    ),
                    enabled = activeButton.value
                ) {
                    Text(
                        text = "Continue",
                        fontFamily = Rajdhani,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreenPasswordValidation(
    navigateback: ()-> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // 1. State Management
    var password by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    // 2. Validation Logic (Computed automatically when password changes)
    val hasMinLength = password.length >= 8
    val hasNumber = password.contains(Regex("[0-9]"))
    val hasSymbol = password.contains(Regex("[!@#$%^&*(),.?\":{}|<>]"))

    // 3. Progress & Color Logic
    val checksPassed = listOf(hasMinLength, hasNumber, hasSymbol).count { it }
    val progressTarget = when (checksPassed) {
        1 -> 0.33f
        2 -> 0.66f
        3 -> 1f
        else -> 0f
    }

    val animatedProgress by animateFloatAsState(targetValue = progressTarget, label = "progress")
    val traceColor by animateColorAsState(
        targetValue = when (checksPassed) {
            3 -> Color.Green
            2 -> Color.Yellow
            1 -> Color.Red
            else -> Color.Gray
        }, label = "color"
    )

    val state = viewModel.state.collectAsState().value

    LaunchedEffect(state.success) {
        if(state.success){
            Log.d("Launched Effect","Launched Effect Triggered")
            navigateToHomeScreen()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Your Password", fontFamily = Rajdhani) },
                navigationIcon = {
                    IconButton(
                        onClick = {navigateback()}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Password",
                fontFamily = Rajdhani,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp), // Correct way to round OutlinedTextField
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

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = traceColor,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )

            // Validation Checklist
            ValidationRow(label = "8 Characters Minimum", isValid = hasMinLength)
            ValidationRow(label = "A Number", isValid = hasNumber)
            ValidationRow(label = "A Symbol", isValid = hasSymbol)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.setPassword(password)
                    viewModel.signUpWithEmail()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(24.dp),
                enabled = checksPassed == 3,
                colors = ButtonDefaults.buttonColors(containerColor = Blue5)
            ) {
                Text("Continue", fontFamily = Rajdhani, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ValidationRow(label: String, isValid: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = isValid,
            onCheckedChange = null // Keep it read-only
        )
        Text(
            text = label,
            fontFamily = Rajdhani,
            fontSize = 14.sp,
            color = if (isValid) Color.Unspecified else Color.Gray
        )
    }
}