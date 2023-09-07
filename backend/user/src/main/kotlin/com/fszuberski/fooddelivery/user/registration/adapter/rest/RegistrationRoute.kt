package com.fszuberski.fooddelivery.user.registration.adapter.rest

import com.fszuberski.fooddelivery.common.rest.handleCall
import com.fszuberski.fooddelivery.common.rest.model.ErrorResponse
import com.fszuberski.fooddelivery.user.registration.adapter.rest.model.RegisterUser
import com.fszuberski.fooddelivery.user.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.user.registration.port.out.UserWithEmailExists
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

private const val BASE_API_ROUTE = "v1"

fun Route.registrationRouting(registerUserUseCase: RegisterUserUseCase) {
    route(BASE_API_ROUTE) {
        route("users") {
            post {
                handleCall(call) {
                    runBlocking {
                        try {
                            val userRegistrationRequest = call.receive<RegisterUser.Request>()
                            val (name, surname, email, password) = userRegistrationRequest
                            val uuid = registerUserUseCase.registerUser(name, surname, email, password)
                            call.respond(HttpStatusCode.Created, RegisterUser.Response(uuid.toString()))
                        } catch (e: Exception) {
                            when (e) {
                                is UserWithEmailExists ->
                                    call.respond(
                                        HttpStatusCode.Conflict,
                                        ErrorResponse.fromException("invalid registration request", e)
                                    )

                                else -> throw e
                            }
                        }
                    }
                }
            }
        }
    }
}

