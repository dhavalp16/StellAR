package com.cosmic_struck.stellar.create_module.presentation.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.cosmic_struck.stellar.common.components.TabSwitcher
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.create_module.presentation.ModelChoice
import com.cosmic_struck.stellar.create_module.presentation.components.ModelViewer
import com.cosmic_struck.stellar.create_module.presentation.components.TabSwitcherModelScreen
import com.cosmic_struck.stellar.create_module.presentation.viewmodel.CreateModuleViewModel
import com.cosmic_struck.stellar.ui.theme.Blue4

@Composable
fun CreateModuleModelScreen(
    navigateToUploadTracker: () -> Unit,
    viewmodel: CreateModuleViewModel = hiltViewModel<CreateModuleViewModel>(),
    modifier: Modifier = Modifier) {
    Scaffold(

    ) {

        val state = viewmodel.state.collectAsState().value

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                viewmodel.uploadModel(uri)
            }
        )

        var modelImageUri by remember {
            mutableStateOf<Uri?>(null)
        }

        val launcher1 = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = {it->
                modelImageUri = it
            }
        )

        LaunchedEffect(Unit) {
            Log.d("Checking Values From Module Screen","moduleName: ${state.moduleName}, description: ${state.description}, classroomId: ${state.classroom_id}, imageUri: ${state.imagePath}, modelUri: ${state.modelPath}, pdfUri: ${state.pdfPath}")
        }
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            TabSwitcher(
                modifier = Modifier
                    .height(40.dp),
                options = listOf(
                    "Upload Model",
                    "Generate Model"
                ),
                onOptionSelected = {it->
                    when(it){
                        0 -> viewmodel.changeModelChoice(0)
                        1 -> viewmodel.changeModelChoice(1)
                    }
                },
                initialIndex = state.selectedOption
            )

            if(state.selectedOption == 0){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue4,
                            contentColor = Color.White
                        ),
                        onClick = {
                            launcher.launch("model/gltf-binary")
                        }
                    ) {
                        Text(
                            text = "Upload Model (GLB File Only)"
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    ModelViewer(
                        modifier = Modifier
                            .fillMaxSize(0.7f)
                            .background(Color.LightGray),
                        modelUri = state.modelPath)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue4,
                            contentColor = Color.White,
                            disabledContainerColor = Blue4.copy(alpha = 0.6f),
                            disabledContentColor = Color.White
                        ),
                        enabled = state.modelPath != null,
                        onClick = {
                            viewmodel.createModule()
                            navigateToUploadTracker()
                        }
                    ) {
                        Text(
                            text = "Continue",
                            color = Color.White,
                            fontFamily = Rajdhani,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            else{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue4,
                            contentColor = Color.White
                        ),
                        onClick = {
                            launcher1.launch("image/*")
                        }
                    ) {
                        Text(
                            text = "Upload Image To Generate Model"
                        )
                    }
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = modelImageUri
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                    )

                    Button(
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue4,
                            contentColor = Color.White,
                            disabledContainerColor = Blue4.copy(alpha = 0.6f),
                            disabledContentColor = Color.White
                        ),
                        enabled = modelImageUri != null,
                        onClick = {

                        }
                    ) {
                        Text(
                            text = "Continue",
                            color = Color.White,
                            fontFamily = Rajdhani,
                            fontSize = 16.sp
                        )
                    }
                }

            }

        }

    }
}