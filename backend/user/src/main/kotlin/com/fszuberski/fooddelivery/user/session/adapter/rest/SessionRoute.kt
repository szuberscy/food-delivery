package com.fszuberski.fooddelivery.user.session.adapter.rest

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fszuberski.fooddelivery.common.authentication.JWTAuth
import com.fszuberski.fooddelivery.common.rest.handleCall
import com.fszuberski.fooddelivery.common.rest.model.ErrorResponse
import com.fszuberski.fooddelivery.user.session.adapter.rest.model.CreateSession
import com.fszuberski.fooddelivery.user.session.port.`in`.InvalidCredentials
import com.fszuberski.fooddelivery.user.session.port.`in`.UserAuthenticationQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

private const val BASE_API_ROUTE = "v1"

fun Route.sessionRouting(jwtAuth: JWTAuth, userAuthenticationQuery: UserAuthenticationQuery) {
    route(BASE_API_ROUTE) {
        route("session") {
            post {
                handleCall(call) {
                    runBlocking {
                        try {
                            val createSessionRequest = call.receive<CreateSession.Request>()
                            // TODO: check username and password
                            val (username, password) = createSessionRequest
                            val authenticationResult =
                                userAuthenticationQuery.getAuthenticationResult(username, password)

                            val publicKey = jwtAuth.publicKey()
                            val privateKey = jwtAuth.privateKey()
                            val token = JWT.create()
                                .withAudience(jwtAuth.audience)
                                .withIssuer(jwtAuth.issuer)
                                .withClaim("username", authenticationResult.username)
                                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                                .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
                            call.respond(HttpStatusCode.Created, CreateSession.Response(token))
                        } catch (e: Exception) {
                            when (e) {
                                is InvalidCredentials ->
                                    call.respond(
                                        HttpStatusCode.Unauthorized,
                                        ErrorResponse.fromException("invalid session creation request", e)
                                    )

                                else -> throw e
                            }
                        }

                    }
                }
            }
        }
    }
    // TODO: session invalidation
    staticFiles(
        remotePath = ".well-known",
        dir = File(object {}.javaClass.getResource("/certs")?.path ?: "")
    )
}