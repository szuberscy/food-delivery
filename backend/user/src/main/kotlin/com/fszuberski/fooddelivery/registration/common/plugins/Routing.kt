package com.fszuberski.fooddelivery.registration.common.plugins

import com.fszuberski.fooddelivery.registration.adapter.rest.userRouting
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    routing {
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
        userRouting(registerUserUseCase = get())
    }
}

