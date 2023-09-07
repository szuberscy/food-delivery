package com.fszuberski.fooddelivery.common.authentication

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import java.util.concurrent.TimeUnit

class JWTAuth(
    val name: String = "auth-jwt",
    val issuer: String,
    val publicKey: String,
    val privateKey: String,
    val audience: String = "/",
    val myRealm: String = "FoodDelivery"
) {
    val jwkProvider: JwkProvider

    init {
        jwkProvider = JwkProviderBuilder(issuer)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }
}