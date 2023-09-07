package com.fszuberski.fooddelivery.common.plugins

import com.fszuberski.fooddelivery.user.registration.adapter.rest.registrationRouting
import com.fszuberski.fooddelivery.user.session.adapter.rest.sessionRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    routing {
        registrationRouting(registerUserUseCase = get())
        sessionRouting(jwtAuth = get(), userAuthenticationQuery = get())
    }
}

