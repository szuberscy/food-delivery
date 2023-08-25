package com.fszuberski.fooddelivery.registration.port.`in`

import com.fszuberski.fooddelivery.registration.core.domain.User

interface RegisterUserUseCase {
    fun registerUser(name: String, surname: String, email: String): User
}
