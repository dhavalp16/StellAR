package com.cosmic_struck.stellar.create_module.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.create_module.presentation.components.ImagePicker
import com.cosmic_struck.stellar.create_module.presentation.viewmodel.CreateModuleViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cosmic_struck.stellar.ui.theme.Blue5

@Composable
fun CreateModuleScreen(
    navigateToModelScreen: () -> Unit,
    viewmodel: CreateModuleViewModel = hiltViewModel<CreateModuleViewModel>(),
    modifier: Modifier = Modifier) {

    val state = viewmodel.state.collectAsState().value

    val pdfUriLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {it->
            viewmodel.uploadPdf(it)
        }
    )

    Scaffold {it->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImagePicker(
                    onChangeImage = viewmodel::uploadImage
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier,
                    value = state.moduleName,
                    onValueChange = {
                        viewmodel.changeModuleName(it)
                    },
                    placeholder = {
                        Text(
                            text = "Module Name",
                            fontFamily = Rajdhani,
                            color = Color.Gray,
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .height(200.dp),
                    value = state.description,
                    onValueChange = {
                        viewmodel.changeDescription(it)
                    },
                    placeholder = {
                        Text(
                            text = "Add Description",
                            fontFamily = Rajdhani,
                            color = Color.Gray,
                        )
                    }
                )

                Spacer(modifier = Modifier.height(80.dp))

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (state.pdfPath != null) {
                        Text(
                            text = "Selected PDF: ${state.pdfPath.lastPathSegment}",
                            fontFamily = Rajdhani
                        )
                    } else {
                        Text(
                            text = "Select PDF to generate Summary and Quiz",
                            fontFamily = Rajdhani
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue5,
                            contentColor = Color.White
                        ),
                        onClick = {
                            // Set the MIME type to PDF
                            pdfUriLauncher.launch("application/pdf")
                        }) {
                        Text("📄 Add PDF")
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .navigationBarsPadding()
                    .padding(bottom = 10.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue5,
                    contentColor = Color.White,
                    disabledContainerColor = Blue5.copy(alpha = 0.6f),
                    disabledContentColor = Color.White
                ),
                enabled = state.pdfPath != null && state.moduleName.trim().isNotEmpty(),
                onClick = {
                    navigateToModelScreen()
                }
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