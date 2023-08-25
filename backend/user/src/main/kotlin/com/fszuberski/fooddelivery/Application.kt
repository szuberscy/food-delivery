package com.fszuberski.fooddelivery

import com.fszuberski.fooddelivery.registration.common.plugins.HttpLogging
import com.fszuberski.fooddelivery.registration.common.plugins.configureKoin
import com.fszuberski.fooddelivery.registration.common.plugins.configureRouting
import com.fszuberski.fooddelivery.registration.common.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin(listOf(appModule))
    configureSerialization()
    configureRouting()

    install(HttpLogging)

}
