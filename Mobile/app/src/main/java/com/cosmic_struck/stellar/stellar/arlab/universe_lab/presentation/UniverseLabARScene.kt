package com.cosmic_struck.stellar.stellar.arlab.universe_lab.presentation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cosmic_struck.stellar.stellar.arlab.universe_lab.engine.CelestialBody
import com.google.ar.core.Frame
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView

@Composable
fun UniverseLabARScene(
    bodies: List<CelestialBody>,
    isPlaced: Boolean,
    onPlaced: () -> Unit,
    onBodyTapped: (CelestialBody) -> Unit,
    getModelPath: (String) -> String,
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val view = rememberView(engine)
    val childNodes = rememberNodes()

    var frame by remember { mutableStateOf<Frame?>(null) }
    var anchorNode by remember { mutableStateOf<AnchorNode?>(null) }
    val bodyNodes = remember { mutableMapOf<String, ModelNode>() }

    // Scale factor to convert simulation coordinates to AR world (meters)
    val scaleFactor = 0.005f // 1 simulation unit = 0.005 meters in AR

    // Update node positions when bodies change
    LaunchedEffect(bodies) {
        if (anchorNode != null && bodyNodes.isNotEmpty()) {
            bodies.forEach { body ->
                bodyNodes[body.id]?.let { node ->
                    node.position = Position(
                        x = body.position.x * scaleFactor,
                        y = body.position.y * scaleFactor,
                        z = body.position.z * scaleFactor
                    )
                }
            }
        }
    }

    ARScene(
        modifier = modifier.fillMaxSize(),
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        childNodes = childNodes,
        planeRenderer = !isPlaced,

        onSessionUpdated = { _, updatedFrame ->
            frame = updatedFrame
        },

        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { motionEvent, node ->
                // If already placed, check for body taps
                if (isPlaced && node != null) {
                    bodies.find { it.id == node.name }?.let { body ->
                        onBodyTapped(body)
                    }
                    return@rememberOnGestureListener
                }

                // Place the solar system
                if (!isPlaced) {
                    val hit = frame
                        ?.hitTest(motionEvent.x, motionEvent.y)
                        ?.firstOrNull {
                            it.isValid(
                                depthPoint = false,
                                point = false
                            )
                        } ?: return@rememberOnGestureListener

                    val anchor = hit.createAnchorOrNull() ?: return@rememberOnGestureListener
                    val newAnchorNode = AnchorNode(engine, anchor)
                    anchorNode = newAnchorNode

                    // Create nodes for each celestial body
                    bodies.forEach { body ->
                        try {
                            val modelPath = getModelPath(body.id)
                            Log.d("UniverseLab", "Loading model for ${body.id}: $modelPath")

                            val modelNode = ModelNode(
                                modelInstance = modelLoader.createModelInstance(modelPath),
                                scaleToUnits = getBodyScale(body.id)
                            ).apply {
                                name = body.id
                                position = Position(
                                    x = body.position.x * scaleFactor,
                                    y = body.position.y * scaleFactor,
                                    z = body.position.z * scaleFactor
                                )
                                isTouchable = true
                            }

                            bodyNodes[body.id] = modelNode
                            newAnchorNode.addChildNode(modelNode)
                        } catch (e: Exception) {
                            Log.e("UniverseLab", "Failed to load model for ${body.id}", e)
                        }
                    }

                    childNodes += newAnchorNode
                    onPlaced()
                }
            }
        )
    )
}

/**
 * Get appropriate scale for each body type
 * These values make the simulation visually appealing in AR
 */
private fun getBodyScale(bodyId: String): Float {
    return when (bodyId) {
        "sun" -> 0.15f
        "earth" -> 0.05f
        "moon" -> 0.02f
        "mars" -> 0.04f
        "jupiter" -> 0.12f
        "saturn" -> 0.10f
        else -> 0.05f
    }
}
