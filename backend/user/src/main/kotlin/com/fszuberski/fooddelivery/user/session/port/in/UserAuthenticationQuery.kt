package com.fszuberski.fooddelivery.user.session.port.`in`

import com.fszuberski.fooddelivery.user.session.core.model.AuthenticationResult

interface UserAuthenticationQuery {
    fun getAuthenticationResult(username: String, password: String): AuthenticationResult
}

class InvalidCredentials(message: String = "invalid credentials") : Exception(message)
