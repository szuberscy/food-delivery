package com.fszuberski.fooddelivery.user.registration.core.service

import com.fszuberski.fooddelivery.user.registration.core.domain.UserRegistration
import com.fszuberski.fooddelivery.user.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.user.registration.port.out.ProduceUserCreatedPort
import com.fszuberski.fooddelivery.user.registration.port.out.SaveUserPort
import java.util.*

class UserRegistrationService(
    private val saveUserPort: SaveUserPort,
    private val produceUserCreatedPort: ProduceUserCreatedPort
) : RegisterUserUseCase {
    override fun registerUser(name: String, surname: String, email: String, password: String): UUID {

        val userRegistration = UserRegistration(name, surname, email, password)

        // TODO: outbox pattern
        val uuid = saveUserPort.save(userRegistration)
//        produceUserCreatedPort.produceUserCreatedEvent(user)

        return uuid
    }
}
