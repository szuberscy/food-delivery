package com.fszuberski.fooddelivery.common.rest

import com.fszuberski.fooddelivery.common.validation.ValidationException
import com.fszuberski.fooddelivery.common.rest.model.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

private val log = KotlinLogging.logger {}

suspend fun handleCall(call: ApplicationCall, block: () -> Unit) {
    try {
        val ct = call.request.contentType()
        if (!ct.match("application/json")) {
            call.respond(HttpStatusCode.UnsupportedMediaType)
        }
        block()
    } catch (e: Exception) {
        when (e) {
            is ValidationException -> {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse.fromException(e))
            }

            else -> {
                log.error(e) { "Unexpected error while handling request" }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}