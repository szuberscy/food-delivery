package com.fszuberski.fooddelivery.user.registration.adapter.persistence.inmemory

import com.fszuberski.fooddelivery.user.registration.core.domain.UserRegistration
import com.fszuberski.fooddelivery.user.registration.port.out.SaveUserPort
import com.fszuberski.fooddelivery.user.registration.port.out.UserWithEmailExists
import java.util.*

// Naive in-memory storage implementation, not thread safe.
class UserInMemoryStorage : SaveUserPort {
    // TODO: real db
    private val storage = mutableMapOf<UUID, UserRegistration>()

    override fun save(userRegistration: UserRegistration): UUID {
        if (!containsWithEmail(userRegistration.email)) {
            return UUID.randomUUID().also { uuid ->
                storage[uuid] = userRegistration
            }
        } else {
            throw UserWithEmailExists()
        }
    }

    private fun containsWithEmail(email: String) =
        storage.values.any { it.email == email }
}