package com.fszuberski.fooddelivery.user.registration.port.`in`

import java.util.UUID

interface RegisterUserUseCase {
    fun registerUser(name: String, surname: String, email: String, password: String): UUID
}
