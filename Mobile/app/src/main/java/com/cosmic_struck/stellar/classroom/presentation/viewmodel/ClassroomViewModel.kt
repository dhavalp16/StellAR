package com.cosmic_struck.stellar.classroom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate.ClassroomHomeScreenDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
    private val classroomHomeScreenDelegate: ClassroomHomeScreenDelegate
) : ViewModel() {

    val homeState: StateFlow<ClassroomHomeScreenState> = classroomHomeScreenDelegate.state

    init {
        // Trigger the initial data load
        viewModelScope.launch {
            classroomHomeScreenDelegate.fetchAllClassroomData()
        }
    }


    fun onToggle(index: Int) {
        classroomHomeScreenDelegate.onToggle(index)
    }
}