package com.fszuberski.fooddelivery.registration.port.out

import com.fszuberski.fooddelivery.registration.core.domain.UserRegistration
import java.util.*

interface SaveUserPort {
    fun save(userRegistration: UserRegistration): UUID
}

class UserWithEmailExists(message: String = "user with email exists") : Exception(message)
