package com.fszuberski.fooddelivery.registration.adapter.rest.model

import com.fszuberski.fooddelivery.registration.core.domain.User
import kotlinx.serialization.Serializable

class RegisterUser {

    @Serializable
    data class Request(
        val name: String = "",
        val surname: String = "",
        val email: String = ""
    )

    @Serializable
    data class Response(val id: String) {
        companion object {
            fun fromUser(user: User) = Response(user.id.toString())
        }
    }
}