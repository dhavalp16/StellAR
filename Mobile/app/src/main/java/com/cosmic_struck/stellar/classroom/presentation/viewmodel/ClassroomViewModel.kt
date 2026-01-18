package com.cosmic_struck.stellar.classroom.presentation.viewmodel

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate.ClassroomHomeScreenDelegate
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate.ClassroomModuleDelegate
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate.QuizManagerDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
    private val classroomHomeScreenDelegate: ClassroomHomeScreenDelegate,
    private val classroomModuleDelegate: ClassroomModuleDelegate,
) : ViewModel() {

    val homeState: StateFlow<ClassroomHomeScreenState> = classroomHomeScreenDelegate.state
    val moduleState: StateFlow<ClassroomModuleState> = classroomModuleDelegate.state

    private var quizManagerDelegate: QuizManagerDelegate? = null

    fun initializeQuizManager(): QuizManagerDelegate {
        if (quizManagerDelegate == null) {
            quizManagerDelegate = QuizManagerDelegate(
                moduleState.value.processInfo?.quiz.orEmpty()
            )
        }
        return quizManagerDelegate!!
    }

    // Reset quiz manager when starting a new quiz
    fun resetQuizManager() {
        quizManagerDelegate = null
    }

    init {
        // Trigger the initial data load
        viewModelScope.launch {
            classroomHomeScreenDelegate.fetchAllClassroomData()
        }
    }

    fun setModuleId(id: Long) {
        viewModelScope.launch {
            classroomModuleDelegate.setModuleId(id)
        }
    }

    /**
     * Process PDF for summary/quiz - uses cache first
     */
    fun processPdf() {
        viewModelScope.launch {
            classroomModuleDelegate.getProcessInfo()
        }
    }

    /**
     * Load module with caching - skips if already loaded
     */
    fun loadModule() {
        viewModelScope.launch {
            classroomModuleDelegate.loadModuleWithCache()
        }
    }

    /**
     * Force refresh module data - clears cache
     */
    fun refreshModule() {
        viewModelScope.launch {
            classroomModuleDelegate.forceRefresh()
        }
    }

    fun onToggle(index: Int) {
        classroomHomeScreenDelegate.onToggle(index)
    }
}