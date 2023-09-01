package com.fszuberski.fooddelivery.registration.core.domain

import com.fszuberski.fooddelivery.registration.common.ValidationError
import com.fszuberski.fooddelivery.registration.common.ensure
import com.fszuberski.fooddelivery.registration.common.validate
import org.apache.commons.validator.routines.EmailValidator
import java.util.*

data class User(
    val id: UUID,
    val name: String,
    val surname: String,
    val email: String
) {
    constructor(name: String, surname: String, email: String) : this(UUID.randomUUID(), name, surname, email)

    init {
        validate(
            ensure(name.trim().isNotBlank()) { ValidationError("name cannot be blank") },
            ensure(surname.trim().isNotBlank()) { ValidationError("surname cannot be blank") },
            ensure(EmailValidator.getInstance().isValid(email.trim())) { ValidationError("invalid email") }
        )
    }
}

