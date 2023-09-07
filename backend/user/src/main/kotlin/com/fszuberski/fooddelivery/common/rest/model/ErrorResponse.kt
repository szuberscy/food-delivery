package com.fszuberski.fooddelivery.common.rest.model

import com.fszuberski.fooddelivery.common.validation.ValidationException
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String, val errors: List<String> = listOf()) {
    companion object {
        fun fromException(message: String, e: Exception) = ErrorResponse(message, listOf(e.message).mapNotNull { it })

        fun fromException(e: ValidationException) = ErrorResponse("validation error", e.errors.map { it.error })
    }
}