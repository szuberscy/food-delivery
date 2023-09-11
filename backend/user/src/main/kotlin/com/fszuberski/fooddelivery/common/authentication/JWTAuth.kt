package com.fszuberski.fooddelivery.common.authentication

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.*
import java.util.concurrent.TimeUnit

class JWTAuth(jwksName: String, environment: ApplicationEnvironment){
    val name: String
    val issuer: String
    val kid: String
    val privateKey: String
    val audience: String
    val realm: String

    val jwkProvider: JwkProvider

    init {
        // TODO: read config in it's own file and pass it here
        name = environment.config.property("jwks.${jwksName}.name").getString()
        issuer = environment.config.property("jwks.${jwksName}.issuer").getString()
        kid = environment.config.property("jwks.${jwksName}.kid").getString()
        privateKey = environment.config.property("jwks.${jwksName}.privateKey").getString() // ?
        audience = environment.config.property("jwks.${jwksName}.audience").getString()
        realm = environment.config.property("jwks.${jwksName}.realm").getString()

        jwkProvider = JwkProviderBuilder(issuer)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }
}
