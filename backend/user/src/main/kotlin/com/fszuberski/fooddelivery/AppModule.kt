package com.fszuberski.fooddelivery

import com.fszuberski.fooddelivery.common.authentication.JWTAuth
import com.fszuberski.fooddelivery.common.configuration.DatabaseConfiguration
import com.fszuberski.fooddelivery.common.persistence.HikariProvider
import com.fszuberski.fooddelivery.user.registration.adapter.kafka.UserEventsProducer
import com.fszuberski.fooddelivery.user.registration.adapter.persistence.UserPersistenceAdapter
import com.fszuberski.fooddelivery.user.registration.core.service.UserRegistrationService
import com.fszuberski.fooddelivery.user.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.user.registration.port.out.ProduceUserCreatedPort
import com.fszuberski.fooddelivery.user.registration.port.out.SaveUserPort
import com.fszuberski.fooddelivery.user.session.core.service.UserAuthService
import com.fszuberski.fooddelivery.user.session.port.`in`.UserAuthenticationQuery
import io.ktor.server.application.*
import org.jdbi.v3.core.Jdbi
import org.koin.dsl.module
import javax.sql.DataSource

fun appModule(environment: ApplicationEnvironment) = module {
    single { DatabaseConfiguration() }
    single<DataSource> { HikariProvider.getDataSource(dbConfiguration = get()) }
    single { Jdbi.create(get<DataSource>()) }

    single<SaveUserPort> { UserPersistenceAdapter(jdbi = get()) }
    single<ProduceUserCreatedPort> { UserEventsProducer() }
    single<RegisterUserUseCase> { UserRegistrationService(saveUserPort = get(), produceUserCreatedPort = get()) }
    single<UserAuthenticationQuery> { UserAuthService() }

    // TODO: generate jwks.json from configuration on app start
    single { JWTAuth("auth-public-jwt", environment) }
}