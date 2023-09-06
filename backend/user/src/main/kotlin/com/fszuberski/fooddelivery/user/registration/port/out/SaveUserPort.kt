package com.fszuberski.fooddelivery.user.registration.port.out

import com.fszuberski.fooddelivery.user.registration.core.domain.UserRegistration
import java.util.*

interface SaveUserPort {
    fun save(userRegistration: UserRegistration): UUID
}

class UserWithEmailExists(message: String = "user with email exists") : Exception(message)
