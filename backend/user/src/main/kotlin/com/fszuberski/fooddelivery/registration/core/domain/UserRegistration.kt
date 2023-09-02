package com.fszuberski.fooddelivery.registration.core.domain

import com.fszuberski.fooddelivery.registration.common.ValidationError
import com.fszuberski.fooddelivery.registration.common.ensure
import com.fszuberski.fooddelivery.registration.common.validate
import org.apache.commons.validator.routines.EmailValidator
import org.mindrot.jbcrypt.BCrypt

class UserRegistration(
    val name: String,
    val surname: String,
    val email: String,
    password: String
) {
    val passwordHash: String

    init {
        validate(
            ensure(name.trim().isNotBlank()) { ValidationError("name cannot be blank") },
            ensure(surname.trim().isNotBlank()) { ValidationError("surname cannot be blank") },
            ensure(EmailValidator.getInstance().isValid(email.trim())) { ValidationError("invalid email") },
            // TODO: validate password
            ensure(password.trim().isNotBlank()) { ValidationError("password cannot be blank") },
        )

        passwordHash = BCrypt.hashpw(password.trim(), BCrypt.gensalt())
    }
}

