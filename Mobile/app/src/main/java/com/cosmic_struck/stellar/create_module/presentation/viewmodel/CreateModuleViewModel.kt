package com.cosmic_struck.stellar.create_module.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.create_module.presentation.CreateModuleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateModuleViewModel @Inject constructor() : ViewModel(){

    private val _state = MutableStateFlow(CreateModuleState())
    val state: StateFlow<CreateModuleState> = _state.asStateFlow()

    fun uploadImage(imagePath: Uri?){
        viewModelScope.launch {
            _state.value = _state.value.copy(imagePath = imagePath)
        }
    }

    fun uploadPdf(imagePath: Uri?){
        viewModelScope.launch {
            _state.value = _state.value.copy(pdfPath = imagePath)
        }
    }

    fun uploadModel(imagePath: Uri?){
        viewModelScope.launch {
            _state.value = _state.value.copy(modelPath = imagePath)
        }
    }

    fun changeDescription(input:String){
        viewModelScope.launch {
            _state.value = _state.value.copy(description = input)
        }
    }
}