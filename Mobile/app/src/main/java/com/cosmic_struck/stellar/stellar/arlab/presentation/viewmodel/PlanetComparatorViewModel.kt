package com.cosmic_struck.stellar.stellar.arlab.presentation.viewmodel


import androidx.lifecycle.ViewModel
import com.cosmic_struck.stellar.stellar.arlab.domain.model.PlanetComparatorModel
import com.cosmic_struck.stellar.stellar.arlab.presentation.states.Feedback
import com.cosmic_struck.stellar.stellar.arlab.presentation.states.PlanetComparatorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlanetComparatorViewModel @Inject constructor(
    ) : ViewModel(){
        private val _state = MutableStateFlow(PlanetComparatorState())
        val state: StateFlow<PlanetComparatorState> = _state
        private val planets = listOf(
            PlanetComparatorModel("earth", "Earth", 6371f, "models/earth.glb"),
            PlanetComparatorModel("mars", "Mars", 3389f, "models/mars.glb"),
            PlanetComparatorModel("jupiter", "Jupiter", 69911f, "models/jupiter.glb"),
            PlanetComparatorModel("saturn", "Saturn", 58232f, "models/saturn.glb")
    )
    init {
        startNewRound()
    }

    fun startNewRound() {
        val (left, right) = planets.shuffled().take(2)

        val correctId =
            if (left.radiusKm > right.radiusKm) left.id else right.id

        _state.value = _state.value.copy(
            leftPlanet = left,
            rightPlanet = right,
            correctPlanetId = correctId,
            feedback = null
        )
    }

    fun onPlanetSelected(planetId: String) {
        val isCorrect = planetId == _state.value.correctPlanetId

        _state.value = _state.value.copy(
            score = if (isCorrect) _state.value.score + 1 else _state.value.score,
            feedback = if (isCorrect) Feedback.CORRECT else Feedback.WRONG
        )
    }

    fun nextRound() {
        _state.value = _state.value.copy(
            round = _state.value.round + 1
        )
        startNewRound()
    }
}