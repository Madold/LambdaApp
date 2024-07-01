package com.markusw.lambda.home.domain.use_cases

import com.markusw.lambda.core.domain.ValidationResult
import javax.inject.Inject

class ValidateCoverUri @Inject constructor() {

    operator fun invoke(uri: String): ValidationResult {
        if (uri.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = "Debes escojer una portada"
            )
        }

        return ValidationResult(
            success = true
        )
    }

}