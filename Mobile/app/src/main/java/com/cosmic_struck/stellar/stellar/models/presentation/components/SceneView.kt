package com.cosmic_struck.stellar.stellar.models.presentation.components

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.filament.Engine
import com.google.android.filament.View
import io.github.sceneview.Scene
import io.github.sceneview.collision.HitResult
import io.github.sceneview.loaders.EnvironmentLoader
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import java.io.File
import java.nio.ByteBuffer

@Composable
fun SceneView(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    environmentLoader: EnvironmentLoader,
    view: View,
    cameraDistance: Float,
    rotationSpeed: Float,
    zoomScale: Float,
    rotationAngle: Float,
    modelPath: String?,
    onChangeRotationAngle: (Float) -> Unit,
    onChangeModelNode: (ModelNode) -> Unit,
    modifier: Modifier = Modifier
) {
    val loadedModelNode = remember { mutableStateOf<ModelNode?>(null) }

        Scene(
            modifier = modifier.fillMaxSize()
                .padding(
                    20.dp
                ),
            engine = engine,
            view = view,
            renderer = rememberRenderer(engine),
            scene = rememberScene(engine),
            modelLoader = modelLoader,
            materialLoader = materialLoader,
            environmentLoader = environmentLoader,
            collisionSystem = rememberCollisionSystem(view),

            // Main Light Setup
            mainLightNode = rememberMainLightNode(engine) {
                intensity = 100_000.0f
            },

            // Camera Setup
            cameraNode = rememberCameraNode(engine) {
                position = Position(
                    x = 0.0f,
                    y = 0.0f,
                    z = cameraDistance
                )
            },

            cameraManipulator = rememberCameraManipulator(),

            // Child Nodes - Model Loading
            childNodes = rememberNodes {
                try {
                    Log.d("SceneView", "Loading model from file system: $modelPath")

                    // Read file from file system (not assets)
                    val file = File(modelPath!!)

                    if (!file.exists()) {
                        Log.e("SceneView", "File does not exist: $modelPath")
                        return@rememberNodes
                    }

                    if (!file.canRead()) {
                        Log.e("SceneView", "Cannot read file: $modelPath")
                        return@rememberNodes
                    }

                    // Read file bytes
                    val buffer = file.inputStream().buffered().use { input ->
                        val bytes = input.readBytes()
                        ByteBuffer.wrap(bytes)
                    }

                    Log.d("SceneView", "File read successfully, size: ${buffer.capacity()} bytes")

                    // Create model instance from buffer
                    // Use the correct method that accepts ByteBuffer for file system files
                    val modelInstance = modelLoader.createModelInstance(buffer)

                    Log.d("SceneView", "Model instance created successfully from file buffer")

                    // Create model node
                    val node = ModelNode(
                        modelInstance = modelInstance
                    ).apply {
                        // Set initial position
                        position = Position(x = 0.0f, y = 0.0f, z = 0.0f)

                        // Set initial scale
                        scale = Scale(x = zoomScale, y = zoomScale, z = zoomScale)

                        // Set initial rotation
                        rotation = Rotation(y = rotationAngle)

                        Log.d("SceneView", "ModelNode configured with initial transforms")
                    }

                    // Store reference and notify callback
                    loadedModelNode.value = node
                    onChangeModelNode(node)

                    // Add to scene
                    add(node)

                    Log.d("SceneView", "Model added to scene successfully")

                } catch (e: Exception) {
                    Log.e("SceneView", "Error loading model: ${e.message}", e)
                    e.printStackTrace()
                }
            },

            // Gesture Handling
            onGestureListener = rememberOnGestureListener(
                onDoubleTapEvent = { _, tappedNode ->
                    if (tappedNode != null) {
                        Log.d("SceneView", "Double tap detected, scaling node")
                        tappedNode.scale = Scale(
                            x = tappedNode.scale.x * 1.2f,
                            y = tappedNode.scale.y * 1.2f,
                            z = tappedNode.scale.z * 1.2f
                        )
                    }
                }
            ),

            // Touch Event Handling
            onTouchEvent = { _: MotionEvent, _: HitResult? ->
                false  // Return false to allow camera manipulation
            },

            // Frame Update - Apply Transformations Every Frame
            onFrame = { _ ->
                loadedModelNode.value?.let { node ->
                    // Update rotation
                    val newRotationAngle = rotationAngle + rotationSpeed * 0.016f  // Smooth rotation (60fps)
                    onChangeRotationAngle(newRotationAngle)

                    node.rotation = Rotation(y = newRotationAngle)

                    // Update scale/zoom
                    node.scale = Scale(
                        x = zoomScale,
                        y = zoomScale,
                        z = zoomScale
                    )
                }
            }
        )
    }