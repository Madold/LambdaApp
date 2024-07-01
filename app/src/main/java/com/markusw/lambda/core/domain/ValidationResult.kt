package com.markusw.lambda.core.domain

data class ValidationResult(
    val success: Boolean,
    val errorMessage: String? = null
)
