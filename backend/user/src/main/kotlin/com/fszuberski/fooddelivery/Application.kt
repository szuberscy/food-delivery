package com.fszuberski.fooddelivery

import com.fszuberski.fooddelivery.common.authentication.JWTAuth
import com.fszuberski.fooddelivery.common.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.get

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureKoin(listOf(appModule(environment)))
    configureSerialization()
    configureAuthentication(get<JWTAuth>())
    configureRouting()
    install(HttpLogging)
}
