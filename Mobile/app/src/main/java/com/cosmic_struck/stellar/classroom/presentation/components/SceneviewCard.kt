package com.cosmic_struck.stellar.classroom.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberView

@Composable
fun SceneViewCard(
    modelUrl: String,
    modifier: Modifier = Modifier) {

    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val cameraNode = rememberCameraNode(engine).apply {
        position = Position(z = 3.5f)
    }
    val view = rememberView(engine)
    val centerNode = rememberNode(engine)

    Scene(
        modifier = modifier,
        engine = engine,
        modelLoader = modelLoader,
        cameraNode = cameraNode,
        childNodes = listOf(centerNode),
        view = view,
        mainLightNode = rememberMainLightNode(engine){
            intensity = 30_000.0f
        }
    ) {

    }

}