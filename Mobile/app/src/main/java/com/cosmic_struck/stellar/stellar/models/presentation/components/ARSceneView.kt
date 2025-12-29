package com.cosmic_struck.stellar.stellar.models.presentation.components//package com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.components
//
//import android.util.Log
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import com.google.ar.core.Config
//import com.google.ar.core.Frame
//import com.google.ar.core.Plane
//import com.google.ar.core.TrackingFailureReason
//import io.github.sceneview.ar.ARScene
//import io.github.sceneview.ar.arcore.createAnchorOrNull
//import io.github.sceneview.ar.arcore.getUpdatedPlanes
//import io.github.sceneview.ar.arcore.isValid
//import io.github.sceneview.ar.node.AnchorNode
//import io.github.sceneview.ar.rememberARCameraNode
//import io.github.sceneview.node.CubeNode
//import io.github.sceneview.node.ModelNode
//import io.github.sceneview.rememberCollisionSystem
//import io.github.sceneview.rememberNodes
//import io.github.sceneview.rememberOnGestureListener
//import androidx.compose.ui.graphics.Color
//import com.google.android.filament.Engine
//import com.google.android.filament.View
//import com.google.ar.core.Anchor
//import io.github.sceneview.loaders.EnvironmentLoader
//import io.github.sceneview.loaders.MaterialLoader
//import io.github.sceneview.loaders.ModelLoader
//import java.io.File
//import java.nio.ByteBuffer
//
//@Composable
//fun ARSceneView(
//    engine: Engine,
//    modelLoader: ModelLoader,
//    materialLoader: MaterialLoader,
//    environmentLoader: EnvironmentLoader,
//    view: View,
//    cameraDistance: Float,
//    rotationSpeed: Float,
//    zoomScale: Float,
//    rotationAngle: Float,
//    modelPath: String,
//    onChangeRotationAngle: (Float) -> Unit,
//    onChangeModelNode: (ModelNode) -> Unit,
//    modifier: Modifier = Modifier
//) {
//
//
//    val cameraNode = rememberARCameraNode(engine)
//    val childNodes = rememberNodes()
//    val collisionSystem = rememberCollisionSystem(view)
//
//    // State tracking
//    val planeRenderer = remember { mutableStateOf(true) }
//    val trackingFailureReason = remember { mutableStateOf<TrackingFailureReason?>(null) }
//    val frame = remember { mutableStateOf<Frame?>(null) }
//    val loadedModelNode = remember { mutableStateOf<ModelNode?>(null) }
//
//    // Validate model path
//
//
//        ARScene(
//            modifier = modifier.fillMaxSize(),
//            engine = engine,
//            view = view,
//            modelLoader = modelLoader,
//            collisionSystem = collisionSystem,
//            childNodes = childNodes,
//            cameraNode = cameraNode,
//
//            // Configure AR session settings
//            sessionConfiguration = { session, config ->
//                Log.d("ARSceneView", "Configuring AR session")
//
//                // Enable depth if supported on the device
//                config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                    true -> {
//                        Log.d("ARSceneView", "Depth mode AUTOMATIC enabled")
//                        Config.DepthMode.AUTOMATIC
//                    }
//                    false -> {
//                        Log.d("ARSceneView", "Depth mode DISABLED")
//                        Config.DepthMode.DISABLED
//                    }
//                }
//
//                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
//                Log.d("ARSceneView", "Instant placement mode set to LOCAL_Y_UP")
//
//                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
//                Log.d("ARSceneView", "Light estimation mode set to ENVIRONMENTAL_HDR")
//            },
//
//            // Enable plane detection visualization
//            planeRenderer = planeRenderer.value,
//
//            // Track camera tracking state changes
//            onTrackingFailureChanged = { reason ->
//                Log.w("ARSceneView", "Tracking failure changed: $reason")
//                trackingFailureReason.value = reason
//            },
//
//            // Frame update callback
//            onSessionUpdated = { session, updatedFrame ->
//                Log.d("ARSceneView", "AR Frame updated - Timestamp: ${updatedFrame.timestamp}")
//                frame.value = updatedFrame
//
//                // Auto-place model on first horizontal plane detected
//                if (childNodes.isEmpty()) {
//                    updatedFrame.getUpdatedPlanes()
//                        .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
//                        ?.let { plane ->
//                            Log.d("ARSceneView", "Horizontal plane detected, creating anchor")
//                            plane.createAnchorOrNull(plane.centerPose)?.let { anchor ->
//                                childNodes += createAnchorNode(
//                                    engine = engine,
//                                    modelLoader = modelLoader,
//                                    materialLoader = materialLoader,
//                                    anchor = anchor,
//                                    modelPath = modelPath,
//                                    zoomScale = zoomScale,
//                                    onModelLoaded = { modelNode ->
//                                        loadedModelNode.value = modelNode
//                                        onChangeModelNode(modelNode)
//                                    }
//                                )
//                            }
//                        }
//                }
//            },
//
//            // Gesture Handling - Tap to place model
//            onGestureListener = rememberOnGestureListener(
//                onSingleTapConfirmed = { motionEvent, node ->
//                    Log.d("ARSceneView", "Single tap detected")
//                    if (node == null) {
//                        val hitResults = frame.value?.hitTest(motionEvent.x, motionEvent.y)
//                        Log.d("ARSceneView", "Hit results count: ${hitResults?.size ?: 0}")
//
//                        hitResults?.firstOrNull {
//                            it.isValid(depthPoint = false, point = false)
//                        }?.createAnchorOrNull()?.let { anchor ->
//                            Log.d("ARSceneView", "Creating anchor node from tap")
//                            planeRenderer.value = false
//
//                            childNodes += createAnchorNode(
//                                engine = engine,
//                                modelLoader = modelLoader,
//                                materialLoader = materialLoader,
//                                anchor = anchor,
//                                modelPath = modelPath,
//                                zoomScale = zoomScale,
//                                onModelLoaded = { modelNode ->
//                                    loadedModelNode.value = modelNode
//                                    onChangeModelNode(modelNode)
//                                }
//                            )
//                        }
//                    }
//                }
//            ),
//
//        )
//    }
//
//
///**
// * Creates an AnchorNode with a loaded 3D model from file system
// */
//private fun createAnchorNode(
//    engine: Engine,
//    modelLoader: ModelLoader,
//    materialLoader: MaterialLoader,
//    anchor: Anchor,
//    modelPath: String,
//    zoomScale: Float,
//    onModelLoaded: (ModelNode) -> Unit
//): AnchorNode {
//    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
//
//    try {
//        Log.d("ARSceneView", "Loading model from file: $modelPath")
//
//        // Read file from file system
//        val file = File(modelPath)
//        if (!file.exists() || !file.canRead()) {
//            Log.e("ARSceneView", "Cannot read model file: $modelPath")
//            return anchorNode
//        }
//
//        // Read file bytes
//        val buffer = file.inputStream().buffered().use { input ->
//            val bytes = input.readBytes()
//            ByteBuffer.wrap(bytes)
//        }
//
//        Log.d("ARSceneView", "File read successfully, size: ${buffer.capacity()} bytes")
//
//        // Create model instance from buffer
//        val modelInstance = modelLoader.createModelInstance(buffer)
//        Log.d("ARSceneView", "Model instance created successfully")
//
//        // Create model node
//        val modelNode = ModelNode(
//            modelInstance = modelInstance,
//            // Scale to fit in a reasonable size in AR space
//            scaleToUnits = 0.5f
//        ).apply {
//            // Model Node needs to be editable for independent rotation from the anchor rotation
//            isEditable = true
//            editableScaleRange = 0.2f..0.75f
//            Log.d("ARSceneView", "ModelNode configured as editable")
//        }
//
//        // Create bounding box for visualization
//        val boundingBoxNode = CubeNode(
//            engine,
//            size = modelNode.extents,
//            center = modelNode.center,
//            materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
//        ).apply {
//            isVisible = false
//        }
//
//        modelNode.addChildNode(boundingBoxNode)
//        anchorNode.addChildNode(modelNode)
//
//        // Show bounding box when editing
//        listOf(modelNode, anchorNode).forEach {
//            it.onEditingChanged = { editingTransforms ->
//                boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
//            }
//        }
//
//        onModelLoaded(modelNode)
//        Log.d("ARSceneView", "AnchorNode created and model added successfully")
//
//    } catch (e: Exception) {
//        Log.e("ARSceneView", "Error creating anchor node: ${e.message}", e)
//        e.printStackTrace()
//    }
//
//    return anchorNode
//}