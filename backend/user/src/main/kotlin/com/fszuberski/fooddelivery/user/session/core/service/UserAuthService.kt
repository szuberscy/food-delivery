package com.fszuberski.fooddelivery.user.session.core.service

import com.fszuberski.fooddelivery.user.session.core.model.AuthenticationResult
import com.fszuberski.fooddelivery.user.session.port.`in`.InvalidCredentials
import com.fszuberski.fooddelivery.user.session.port.`in`.UserAuthenticationQuery
import org.mindrot.jbcrypt.BCrypt

class UserAuthService: UserAuthenticationQuery {

    override fun getAuthenticationResult(username: String, password: String): AuthenticationResult {
        // TODO: retrieve user details from db
        val authDetails = AuthenticationResult("username", BCrypt.hashpw("123password", BCrypt.gensalt()) )

        if (!BCrypt.checkpw(password, authDetails.passwordHash)) {
            throw InvalidCredentials()
        }

        return authDetails
    }
}