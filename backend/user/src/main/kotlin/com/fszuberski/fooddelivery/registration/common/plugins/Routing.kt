package com.fszuberski.fooddelivery.registration.common.plugins

import com.fszuberski.fooddelivery.registration.adapter.rest.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    routing {
        userRouting(registerUserUseCase = get())
    }
}

