package com.fszuberski.fooddelivery.registration.common

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.left
import arrow.core.right

fun ensure(value: Boolean, lazyValidationError: () -> ValidationError): Either<ValidationError, Unit> =
    if (!value) {
        lazyValidationError().left()
    } else {
        Unit.right()
    }

fun validate(vararg validationResults: Either<ValidationError, Unit>) {
    val validationErrors = validationResults.mapNotNull {
        when (it) {
            is Left -> it.value
            is Right -> null
        }
    }

    if (validationErrors.isEmpty().not()) {
        throw ValidationException(validationErrors)
    }
}
