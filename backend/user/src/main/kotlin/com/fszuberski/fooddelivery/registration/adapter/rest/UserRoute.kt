package com.fszuberski.fooddelivery.registration.adapter.rest

import com.fszuberski.fooddelivery.registration.adapter.rest.model.ErrorResponse
import com.fszuberski.fooddelivery.registration.adapter.rest.model.RegisterUser
import com.fszuberski.fooddelivery.registration.common.ValidationException
import com.fszuberski.fooddelivery.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.registration.port.out.UserWithEmailExists
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

private val log = KotlinLogging.logger {}
private const val BASE_API_ROUTE = "v1"

fun Route.userRouting(registerUserUseCase: RegisterUserUseCase) {
    route(BASE_API_ROUTE) {
        route("users") {
            post {
                handleCall(call) {
                    runBlocking {
                        try {
                            val userRegistrationRequest = call.receive<RegisterUser.Request>()
                            val (name, surname, email) = userRegistrationRequest
                            val registeredUser = registerUserUseCase.registerUser(name, surname, email)
                            call.respond(HttpStatusCode.Created, RegisterUser.Response.fromUser(registeredUser))
                        } catch (e: Exception) {
                            when (e) {
                                is UserWithEmailExists -> {
                                    call.respond(HttpStatusCode.Conflict, ErrorResponse.fromException("invalid registration request", e))
                                }

                                else -> throw e
                            }
                        }
                    }
                }
            }
        }

        route("session") {
            post {

            }
        }
    }

}

suspend fun handleCall(call: ApplicationCall, block: () -> Unit) {
    try {
        val ct = call.request.contentType()
        if (!ct.match("application/json")) {
            call.respond(HttpStatusCode.UnsupportedMediaType)
        }
        block()
    } catch (e: Exception) {
        log.info { ">>> Exception 2"}
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
