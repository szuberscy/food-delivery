package com.fszuberski.fooddelivery.common.validation

class ValidationException(val errors: List<ValidationError>) : Exception(errors.toString()) {
    constructor(error: ValidationError) : this(listOf(error))
}

data class ValidationError(val error: String)
