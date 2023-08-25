package com.fszuberski.fooddelivery.registration.port.out

import com.fszuberski.fooddelivery.registration.core.domain.User

interface SaveUserPort {
    fun save(user: User)
}

class UserWithEmailExists(message: String = "user with email exists") : Exception(message)
