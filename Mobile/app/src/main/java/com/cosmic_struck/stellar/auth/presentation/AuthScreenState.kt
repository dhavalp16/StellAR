package com.cosmic_struck.stellar.auth.presentation

data class AuthScreenState(
    val isLoading : Boolean = false,
    val email : String = "",
    val password: String = "",
    val username: String = "",
    val error : String = "",
    val success: Boolean = false
)
