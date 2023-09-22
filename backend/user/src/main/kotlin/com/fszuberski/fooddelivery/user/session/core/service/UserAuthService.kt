package com.fszuberski.fooddelivery.user.session.core.service

import com.fszuberski.fooddelivery.user.session.core.domain.AuthDetails
import com.fszuberski.fooddelivery.user.session.port.`in`.InvalidCredentials
import com.fszuberski.fooddelivery.user.session.port.`in`.UserAuthenticationQuery
import com.fszuberski.fooddelivery.user.session.port.out.GetAuthDetailsPort
import org.mindrot.jbcrypt.BCrypt

class UserAuthService(
    private val getAuthDetailsPort: GetAuthDetailsPort
) : UserAuthenticationQuery {

    override fun getAuthenticationResult(username: String, password: String): AuthDetails {
        val authDetails = getAuthDetailsPort.getByUsername(username)

        authDetails?.let {
            if (BCrypt.checkpw(password, authDetails.passwordHash)) return authDetails
        }

        throw InvalidCredentials()
    }
}