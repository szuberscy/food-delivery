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
import io.ktor.server.application.*
import org.jdbi.v3.core.Jdbi
import org.koin.dsl.module
import java.util.*

fun appModule(environment: ApplicationEnvironment) = module {
    single<SaveUserPort> { UserInMemoryStorage() }
    single<ProduceUserCreatedPort> { UserEventsProducer() }
    single<RegisterUserUseCase> { UserRegistrationService(saveUserPort = get(), produceUserCreatedPort = get()) }
    single<UserAuthenticationQuery> { UserAuthService() }

    // TODO: cleanup jdbi stuff
    val jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/user?user=fd_user&password=password")
    jdbi.withHandle<Unit, Exception> { handle ->
        handle.execute("select * from users")
    }

    jdbi.withHandle<Unit, Exception> { handle ->
        handle.createUpdate("insert into users(id) values (:id)")
            .bind("id", UUID.randomUUID())
            .execute()
    }

    // TODO: generate jwks.json from configuration on app start
    single { JWTAuth("auth-public-jwt", environment) }
}