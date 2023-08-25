package com.fszuberski.fooddelivery.registration.port.out

import com.fszuberski.fooddelivery.registration.core.domain.User

interface ProduceUserCreatedPort {
    fun produceUserCreatedEvent(user: User)
}