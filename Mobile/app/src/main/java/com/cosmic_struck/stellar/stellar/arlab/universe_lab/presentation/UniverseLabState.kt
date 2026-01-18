package com.cosmic_struck.stellar.stellar.arlab.universe_lab.presentation

import com.cosmic_struck.stellar.stellar.arlab.universe_lab.engine.CelestialBody

data class UniverseLabState(
    val bodies: List<CelestialBody> = emptyList(),
    val isPlaying: Boolean = false,
    val timeScale: Float = 1f,
    val isPlaced: Boolean = false,
    val selectedBody: CelestialBody? = null,
    val simulationTime: Float = 0f
)
