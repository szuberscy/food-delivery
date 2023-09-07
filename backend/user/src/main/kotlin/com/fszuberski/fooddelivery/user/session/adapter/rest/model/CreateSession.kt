package com.fszuberski.fooddelivery.user.session.adapter.rest.model

import kotlinx.serialization.Serializable

class CreateSession {

    @Serializable
    data class Request(
        val username: String = "",
        val password: String = ""
    )

    @Serializable
    data class Response(val token: String)
}