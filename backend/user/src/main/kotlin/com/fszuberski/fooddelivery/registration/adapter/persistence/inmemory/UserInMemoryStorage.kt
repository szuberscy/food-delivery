package com.fszuberski.fooddelivery.registration.adapter.persistence.inmemory

import com.fszuberski.fooddelivery.registration.core.domain.User
import com.fszuberski.fooddelivery.registration.port.out.SaveUserPort
import com.fszuberski.fooddelivery.registration.port.out.UserWithEmailExists
import java.util.*

// Naive in-memory storage implementation, not thread safe.
class UserInMemoryStorage : SaveUserPort {
    private val storage = mutableMapOf<UUID, User>()

    override fun save(user: User) {
        if (storage[user.id] == null && !containsWithEmail(user.email)) {
            storage[user.id] = user
        } else {
            throw UserWithEmailExists()
        }
    }

    private fun containsWithEmail(email: String) =
        storage.values.any { it.surname == email }
}