package com.fszuberski.fooddelivery.common.authentication

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.*
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

class JWTAuth(jwksName: String, environment: ApplicationEnvironment) {
    val name: String
    val issuer: String
    val audience: String
    val realm: String
    private val kty: String
    private val kid: String
    private val privateKeyBase64: String

    val jwkProvider: JwkProvider

    init {
        // TODO: read config in it's own file and pass it here
        name = environment.config.property("jwks.${jwksName}.name").getString()
        issuer = environment.config.property("jwks.${jwksName}.issuer").getString()
        audience = environment.config.property("jwks.${jwksName}.audience").getString()
        realm = environment.config.property("jwks.${jwksName}.realm").getString()
        kty = environment.config.property("jwks.${jwksName}.kty").getString()
        kid = environment.config.property("jwks.${jwksName}.kid").getString()
        privateKeyBase64 = environment.config.property("jwks.${jwksName}.privateKey").getString() // ?

        jwkProvider = JwkProviderBuilder(issuer)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }

    fun publicKey(): PublicKey = jwkProvider.get(kid).publicKey
    fun privateKey(): PrivateKey =
        PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64))
            .let {
                KeyFactory.getInstance(kty).generatePrivate(it)
            }
}
