package com.fszuberski.fooddelivery.user.session.adapter.persistence

import com.fszuberski.fooddelivery.user.registration.adapter.persistence.withExceptionTranslatingHandle
import com.fszuberski.fooddelivery.user.session.core.domain.AuthDetails
import com.fszuberski.fooddelivery.user.session.port.out.GetAuthDetailsPort
import org.jdbi.v3.core.Jdbi

class UserAuthDetailsPersistenceAdapter(
    private val jdbi: Jdbi
) : GetAuthDetailsPort {

    override fun getByUsername(username: String): AuthDetails? {
        try {
            return jdbi.withExceptionTranslatingHandle<AuthDetails, Exception> { handle ->
                handle.createQuery(
                    """
                    select email, password_hash
                    from users
                    where email ilike :email
                    """.trimIndent()
                )
                    .bind("email", username)
                    .map { rs, _ ->
                        AuthDetails(
                            rs.getString("email"),
                            rs.getString("password_hash")
                        )
                    }
                    .list()
                    .first()
            }
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException -> return null
                else -> throw e
            }
        }
    }
}