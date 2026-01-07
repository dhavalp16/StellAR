package com.cosmic_struck.stellar.create_module.presentation

import android.net.Uri

data class CreateModuleState(
    val isLoading : Boolean = false,
    val error: String? = null,
    val imagePath: Uri? = null,
    val pdfPath: Uri? = null,
    val modelPath: Uri? = null,
    val description : String = "",
)
