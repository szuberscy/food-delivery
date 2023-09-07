package com.fszuberski.fooddelivery.user.session.core.model

data class AuthenticationResult(
    val username: String,
    val passwordHash: String
)