package com.fszuberski.fooddelivery.user.registration.port.out

import com.fszuberski.fooddelivery.user.registration.core.domain.UserRegistration
import java.util.*

interface ProduceUserCreatedPort {
    fun produceUserCreatedEvent(userId: UUID, userRegistration: UserRegistration)
}