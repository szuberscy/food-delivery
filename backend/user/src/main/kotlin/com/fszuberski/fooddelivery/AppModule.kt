package com.fszuberski.fooddelivery

import com.fszuberski.fooddelivery.registration.adapter.kafka.UserEventsProducer
import com.fszuberski.fooddelivery.registration.adapter.persistence.inmemory.UserInMemoryStorage
import com.fszuberski.fooddelivery.registration.core.service.UserService
import com.fszuberski.fooddelivery.registration.port.`in`.RegisterUserUseCase
import com.fszuberski.fooddelivery.registration.port.out.ProduceUserCreatedPort
import com.fszuberski.fooddelivery.registration.port.out.SaveUserPort
import org.koin.dsl.module

val appModule = module {
    single<SaveUserPort> { UserInMemoryStorage() }
    single<ProduceUserCreatedPort> { UserEventsProducer() }
    single<RegisterUserUseCase> { UserService(saveUserPort = get(), produceUserCreatedPort = get()) }
}