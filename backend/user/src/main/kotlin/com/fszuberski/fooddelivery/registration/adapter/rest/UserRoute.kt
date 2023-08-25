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

private val log = KotlinLogging.logger {}
private const val BASE_API_ROUTE = "v1"

fun Route.userRouting(registerUserUseCase: RegisterUserUseCase) {
    route(BASE_API_ROUTE) {
        route("users") {
            post {
                try {
                    val userRegistrationRequest = call.receive<RegisterUser.Request>()

                    val (name, surname, email) = userRegistrationRequest
                    val registeredUser = registerUserUseCase.registerUser(name, surname, email)
                    call.respond(HttpStatusCode.Created, RegisterUser.Response.fromUser(registeredUser))
                } catch (e: Exception) {
                    when (e) {
                        is ValidationException -> {
                            log.info { "Validation errors: ${e.message}" }
                            call.respond(HttpStatusCode.BadRequest, ErrorResponse.fromException(e))
                        }

                        is UserWithEmailExists -> {
                            log.info { "User with email exists" }
                            call.respond(HttpStatusCode.Conflict, ErrorResponse.fromException(e))
                        }

                        else -> {
                            log.error(e) { "Unexpected error while handling request" }
                            call.respond(HttpStatusCode.InternalServerError)
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


