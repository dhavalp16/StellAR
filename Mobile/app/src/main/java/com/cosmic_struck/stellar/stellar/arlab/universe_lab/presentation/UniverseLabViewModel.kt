package com.cosmic_struck.stellar.stellar.arlab.universe_lab.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.stellar.arlab.universe_lab.engine.CelestialBody
import com.cosmic_struck.stellar.stellar.arlab.universe_lab.engine.UniverseSimulator
import com.cosmic_struck.stellar.stellar.arlab.universe_lab.engine.Vector3D
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniverseLabViewModel @Inject constructor() : ViewModel() {

    private val simulator = UniverseSimulator()
    private var simulationJob: Job? = null

    private val _state = MutableStateFlow(UniverseLabState())
    val state: StateFlow<UniverseLabState> = _state.asStateFlow()

    // Model paths for each celestial body
    val bodyModelPaths = mapOf(
        "sun" to "models/sun.glb",
        "earth" to "models/earth.glb",
        "moon" to "models/moon.glb",
        "mars" to "models/mars.glb",
        "jupiter" to "models/jupiter.glb",
        "saturn" to "models/saturn.glb"
    )

    init {
        initializeSolarSystem()
    }

    private fun initializeSolarSystem() {
        simulator.createSolarSystem()
        updateBodiesState()
    }

    private fun updateBodiesState() {
        _state.update { currentState ->
            currentState.copy(
                bodies = simulator.getBodies(),
                simulationTime = simulator.getEngine().getSimulationTime()
            )
        }
    }

    fun onPlaced() {
        _state.update { it.copy(isPlaced = true) }
    }

    fun play() {
        if (_state.value.isPlaying) return

        _state.update { it.copy(isPlaying = true) }

        simulationJob = viewModelScope.launch {
            while (isActive && _state.value.isPlaying) {
                simulator.step()
                updateBodiesState()
                delay(33L) // ~30 FPS
            }
        }
    }

    fun pause() {
        _state.update { it.copy(isPlaying = false) }
        simulationJob?.cancel()
        simulationJob = null
    }

    fun togglePlayPause() {
        if (_state.value.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun reset() {
        pause()
        simulator.getEngine().reset()
        simulator.createSolarSystem()
        updateBodiesState()
        _state.update { it.copy(isPlaced = false) }
    }

    fun setTimeScale(scale: Float) {
        simulator.getEngine().setTimeScale(scale.coerceIn(0.1f, 5f))
        _state.update { it.copy(timeScale = scale) }
    }

    fun selectBody(body: CelestialBody?) {
        _state.update { it.copy(selectedBody = body) }
    }

    fun getModelPath(bodyId: String): String {
        return bodyModelPaths[bodyId] ?: "models/earth.glb"
    }

    override fun onCleared() {
        super.onCleared()
        simulationJob?.cancel()
    }
}
