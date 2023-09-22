package com.fszuberski.fooddelivery.user.session.port.`in`

import com.fszuberski.fooddelivery.user.session.core.domain.AuthDetails

interface UserAuthenticationQuery {
    fun getAuthenticationResult(username: String, password: String): AuthDetails
}

class InvalidCredentials(message: String = "invalid credentials") : Exception(message)
