package com.fszuberski.fooddelivery

import com.fszuberski.fooddelivery.common.authentication.JWTAuth
import com.fszuberski.fooddelivery.user.registration.adapter.kafka.UserEventsProducer
import com.fszuberski.fooddelivery.user.registration.adapter.persistence.inmemory.UserInMemoryStorage
import com.fszuberski.fooddelivery.user.registration.core.service.UserRegistrationService
import com.fszuberski.fooddelivery.user.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.user.registration.port.out.ProduceUserCreatedPort
import com.fszuberski.fooddelivery.user.registration.port.out.SaveUserPort
import com.fszuberski.fooddelivery.user.session.core.service.UserAuthService
import com.fszuberski.fooddelivery.user.session.port.`in`.UserAuthenticationQuery
import org.koin.dsl.module

val appModule = module {
    single<SaveUserPort> { UserInMemoryStorage() }
    single<ProduceUserCreatedPort> { UserEventsProducer() }
    single<RegisterUserUseCase> { UserRegistrationService(saveUserPort = get(), produceUserCreatedPort = get()) }
    single<UserAuthenticationQuery> { UserAuthService() }

    // TODO: Read the jwt properties from the config file
    // TODO: generate proper jwks, these are copied from jetbrains examples
    val name = "auth-jwt"
    val issuer = "http://0.0.0.0:8081"
    val publicKeyString = "1234asdf"
    val privateKeyString = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAtfJaLrzXILUg1U3N1KV8yJr92GHn5OtYZR7qWk1Mc4cy4JGjklYup7weMjBD9f3bBVoIsiUVX6xNcYIr0Ie0AQIDAQABAkEAg+FBquToDeYcAWBe1EaLVyC45HG60zwfG1S4S3IB+y4INz1FHuZppDjBh09jptQNd+kSMlG1LkAc/3znKTPJ7QIhANpyB0OfTK44lpH4ScJmCxjZV52mIrQcmnS3QzkxWQCDAiEA1Tn7qyoh+0rOO/9vJHP8U/beo51SiQMw0880a1UaiisCIQDNwY46EbhGeiLJR1cidr+JHl86rRwPDsolmeEF5AdzRQIgK3KXL3d0WSoS//K6iOkBX3KMRzaFXNnDl0U/XyeGMuUCIHaXv+n+Brz5BDnRbWS+2vkgIe9bUNlkiArpjWvX+2we"
    val audience = "http://0.0.0.0:8081/" // application specific targets that must verify the jwt (https://stackoverflow.com/a/41237822)
    val myRealm = "food delivery service"
    single { JWTAuth(name, issuer, publicKeyString, privateKeyString, audience, myRealm) }
}