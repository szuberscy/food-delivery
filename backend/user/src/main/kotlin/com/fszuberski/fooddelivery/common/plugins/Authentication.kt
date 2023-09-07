package com.fszuberski.fooddelivery.common.plugins

import com.fszuberski.fooddelivery.common.authentication.JWTAuth
import com.fszuberski.fooddelivery.common.rest.model.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication(jwtAuth: JWTAuth) {

    install(Authentication) {
        jwt(jwtAuth.name) {
            realm = jwtAuth.myRealm
            verifier(jwtAuth.jwkProvider, jwtAuth.issuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                // TODO
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("authorization error", listOf("Token is not valid or has expired"))
                )
            }
        }
    }
}
