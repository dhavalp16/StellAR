package com.cosmic_struck.stellar.stellar.models.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.stellar.models.presentation.components.ControlPanel
import com.cosmic_struck.stellar.stellar.models.presentation.components.ModelViewerTopAppBar
import com.cosmic_struck.stellar.stellar.models.presentation.components.SceneView
import com.cosmic_struck.stellar.stellar.models.presentation.viewmodel.ModelViewScreenViewModel
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberView
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelViewerScreen(
    navigateBack: () -> Unit = {},
    viewModel: ModelViewScreenViewModel = hiltViewModel<ModelViewScreenViewModel>(),
) {
    val context = LocalContext.current
    // Single state for this screen
    val state = viewModel.state.value

    BackgroundScaffold(
        topBar = {
            ModelViewerTopAppBar(
                onNavigateBack = navigateBack,
                name = state.modelTitle
            )
        }
    ) {
        if (state.isLoadingModel) {
            Log.d("ModelViewerScreen", "✅ Showing LOADING state")
            val composition =
                rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.space_shuttle1))
            val isPlaying = remember { mutableStateOf(true) }
            Box(modifier = it.fillMaxSize()) {
                LottieAnimation(
                    composition = composition.value,
                    isPlaying = isPlaying.value,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    contentScale = ContentScale.Fit
                )
            }
        } else if (state.modelError.isNotEmpty()) {
            Log.d("ModelViewerScreen", "❌ Showing ERROR state: ${state.modelError}")
            Column(
                modifier = it
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error loading model",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.modelError,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        Log.d("ModelViewerScreen", "Retry clicked")
                        viewModel.downloadModel()
                    }
                ) {
                    Text("Retry")
                }
            }
        } else {
//            Log.d("ModelViewerScreen", "🎬 Showing CONTENT state")
            Box(modifier = it.fillMaxSize()) {
                val engine = rememberEngine()
                val modelLoader = rememberModelLoader(engine)
                val materialLoader = rememberMaterialLoader(engine)
                val environmentLoader = rememberEnvironmentLoader(engine)
                val view = rememberView(engine)

                DisposableEffect(engine, view) {
                    onDispose {
                        try {
                            Log.d("ModelViewerScreen", "🧹 Starting engine cleanup...")
                            engine.destroyView(view)
                            Thread.sleep(1000)
                        } catch (e: Exception) {
                            Log.e("ModelViewerScreen", "Cleanup error: ${e.message}", e)
                        }
                    }
                }

                val isValidModelPath = remember(state.modelPath) {
                    Log.d("ModelViewerScreen", "🔍 Validating model path: '${state.modelPath}'")
                    if (state.modelPath.isEmpty()) {
                        Log.d("ModelViewerScreen", "⚠️ Model path is empty")
                        false
                    } else {
                        try {
                            val file = File(state.modelPath)
                            val isValid = file.exists() && file.isFile && file.canRead() && file.length() > 0
                            Log.d(
                                "ModelViewerScreen",
                                "File validation: exists=${file.exists()}, isFile=${file.isFile}, canRead=${file.canRead()}, size=${file.length()}, isValid=$isValid"
                            )
                            isValid
                        } catch (e: Exception) {
                            Log.e("ModelViewerScreen", "Validation error: ${e.message}")
                            false
                        }
                    }
                }

                if (isValidModelPath && state.modelPath.isNotEmpty()) {
                    Log.d("ModelViewerScreen", "✅ Rendering SceneView with model: ${state.modelPath}")
                    SceneView(
                        modifier = Modifier.align(Alignment.Center),
                        cameraDistance = state.cameraDistance,
                        rotationSpeed = state.rotationSpeed,
                        zoomScale = state.zoomScale,
                        rotationAngle = state.rotationAngle,
                        modelPath = state.modelPath,
                        onChangeRotationAngle = { viewModel.onChangeRotationAngle(it) },
                        onChangeModelNode = { viewModel.onChangeModelNode(it) },
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        environmentLoader = environmentLoader,
                        view = view,
                    )
                } else {
                    Log.d("ModelViewerScreen", "⏳ Waiting for valid model path...")
                }

//                ControlPanel(
//                    modifier = Modifier.align(Alignment.BottomCenter),
//                    rotationSpeed = state.rotationSpeed,
//                    zoomScale = state.zoomScale,
//                    cameraDistance = state.cameraDistance,
//                    onRotationSpeedChange = { viewModel.onChangeRotation(it) },
//                    onZoomScaleChange = { viewModel.onChangeZoomScale(it) },
//                    onCameraDistanceChange = { viewModel.onChangeCameraDistance(it) },
//                    scene = state.scene,
//                    onToggleScene = { viewModel.toggleScene() }
//                )
            }
        }
    }
}