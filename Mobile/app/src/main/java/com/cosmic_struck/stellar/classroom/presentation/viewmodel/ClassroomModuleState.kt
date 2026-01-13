package com.cosmic_struck.stellar.classroom.presentation.viewmodel

data class ClassroomModuleState(
    val module_name: String = "",
    val module_id: Int? = null,
    val pdf_url : String = "",
    val model_url : String = "",

    val pdf_path: String = "",
    val model_path : String = "",
    val isLoading : Boolean = false,
    val error : String = ""
)
