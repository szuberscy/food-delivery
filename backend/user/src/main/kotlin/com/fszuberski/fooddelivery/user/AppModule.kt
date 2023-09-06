package com.fszuberski.fooddelivery.user

import com.fszuberski.fooddelivery.user.registration.adapter.kafka.UserEventsProducer
import com.fszuberski.fooddelivery.user.registration.adapter.persistence.inmemory.UserInMemoryStorage
import com.fszuberski.fooddelivery.user.registration.core.service.UserRegistrationService
import com.fszuberski.fooddelivery.user.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.user.registration.port.out.ProduceUserCreatedPort
import com.fszuberski.fooddelivery.user.registration.port.out.SaveUserPort
import org.koin.dsl.module

val appModule = module {
    single<SaveUserPort> { UserInMemoryStorage() }
    single<ProduceUserCreatedPort> { UserEventsProducer() }
    single<RegisterUserUseCase> { UserRegistrationService(saveUserPort = get(), produceUserCreatedPort = get()) }
}