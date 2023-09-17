package com.fszuberski.fooddelivery.user.registration.adapter.persistence

import com.fszuberski.fooddelivery.user.registration.core.domain.UserRegistration
import com.fszuberski.fooddelivery.user.registration.port.out.SaveUserPort
import com.fszuberski.fooddelivery.user.registration.port.out.UserWithEmailExists
import org.jdbi.v3.core.Jdbi
import java.util.*

class UserPersistenceAdapter(val jdbi: Jdbi) : SaveUserPort {

    override fun save(userRegistration: UserRegistration): UUID {
        val uuid = UUID.randomUUID()
        try {
            jdbi.withExceptionTranslatingHandle<Unit, Exception> { handle ->
                handle.createUpdate(
                    """
                        insert into users(id, email, name, surname, password_hash) 
                        values (:id, :email, :name, :surname, :password_hash)"""
                        .trimIndent()
                )
                    .bind("id", uuid)
                    .bind("email", userRegistration.email)
                    .bind("name", userRegistration.name)
                    .bind("surname", userRegistration.surname)
                    .bind("password_hash", userRegistration.passwordHash)
                    .execute()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                // naive mapping; no other unique violation can exist for table
                is UniqueViolation -> throw UserWithEmailExists(cause = e)
                else -> throw e
            }
        }

        return uuid
    }
}