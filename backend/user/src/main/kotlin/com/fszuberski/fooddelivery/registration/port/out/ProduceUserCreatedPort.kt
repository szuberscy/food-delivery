package com.fszuberski.fooddelivery.registration.port.out

import com.fszuberski.fooddelivery.registration.core.domain.UserRegistration
import java.util.*

interface ProduceUserCreatedPort {
    fun produceUserCreatedEvent(userId: UUID, userRegistration: UserRegistration)
}