package com.fszuberski.fooddelivery.user.registration.core.domain

import com.fszuberski.fooddelivery.common.validation.ValidationError
import com.fszuberski.fooddelivery.common.validation.ensure
import com.fszuberski.fooddelivery.common.validation.validate
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

            ensure(password.trim().length >= 8) { ValidationError("password must contain at least 8 characters") },
            ensure(password.trim().contains(Regex("[a-zA-Z]"))) { ValidationError("password must contain at least one letter") },
            ensure(password.trim().contains(Regex("\\d"))) { ValidationError("password must contain at least one digit") },
        )

        passwordHash = BCrypt.hashpw(password.trim(), BCrypt.gensalt())
    }
}

