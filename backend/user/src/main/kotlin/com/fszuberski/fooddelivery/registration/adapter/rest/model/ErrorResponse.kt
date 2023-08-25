package com.fszuberski.fooddelivery.registration.adapter.rest.model

import com.fszuberski.fooddelivery.registration.common.ValidationException
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String, val errors: List<String> = listOf()) {
    companion object {
        fun fromException(e: Exception) = ErrorResponse(e.message ?: "")

        fun fromException(e: ValidationException) = ErrorResponse("validation exception", e.errors.map { it.error })
    }
}