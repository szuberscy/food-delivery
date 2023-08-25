package com.fszuberski.fooddelivery.registration.core.service

import com.fszuberski.fooddelivery.registration.core.domain.User
import com.fszuberski.fooddelivery.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.registration.port.out.ProduceUserCreatedPort
import com.fszuberski.fooddelivery.registration.port.out.SaveUserPort

class UserService(
    private val saveUserPort: SaveUserPort,
    private val produceUserCreatedPort: ProduceUserCreatedPort
) : RegisterUserUseCase {
    override fun registerUser(name: String, surname: String, email: String): User {
        val user = User(name, surname, email)

        // TODO: outbox pattern
        saveUserPort.save(user)
//        produceUserCreatedPort.produceUserCreatedEvent(user)

        return user
    }
}
