package com.fszuberski.fooddelivery.registration.adapter.rest.model

import kotlinx.serialization.Serializable

class RegisterUser {

    @Serializable
    data class Request(
        val name: String = "",
        val surname: String = "",
        val email: String = "",
        val password: String
    )

    @Serializable
    data class Response(val id: String)
}