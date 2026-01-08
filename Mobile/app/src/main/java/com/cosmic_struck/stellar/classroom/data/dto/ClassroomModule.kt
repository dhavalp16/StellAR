package com.cosmic_struck.stellar.classroom.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClassroomModule(
    val module_id: Int,
    val module_name: String,
    val module_desc: String,
    val image_url: String,
    val model_url: String,
    val pdf_url: String,
    val created_at: String
)
