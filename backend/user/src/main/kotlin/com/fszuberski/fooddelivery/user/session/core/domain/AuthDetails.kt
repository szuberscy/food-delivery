package com.fszuberski.fooddelivery.user.session.core.domain

data class AuthDetails(
    val username: String,
    val passwordHash: String
)