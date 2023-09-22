package com.fszuberski.fooddelivery.user.registration.adapter.kafka

import com.fszuberski.fooddelivery.user.registration.core.domain.UserRegistration
import com.fszuberski.fooddelivery.user.UserCreatedEvent
import com.fszuberski.fooddelivery.user.registration.port.out.ProduceUserCreatedPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

private val log = KotlinLogging.logger {}
private const val TOPIC_NAME = "user-events"

class UserEventsProducer : ProduceUserCreatedPort {
    override fun produceUserCreatedEvent(userId: UUID, userRegistration: UserRegistration) {
        // TODO: immediately closes the producer once a message is produced.
        // Could be cached so we don't reestablish connections.
        Configuration.producer().use { producer ->
            try {
                val userCreatedEvent = UserCreatedEvent(userId.toString(), userRegistration.name, userRegistration.surname, userRegistration.email, LocalDateTime.now().toString())
                producer.send(
                    ProducerRecord(TOPIC_NAME, userId.toString(), userCreatedEvent)
                ) { meta: RecordMetadata, e: Exception? ->
                    when (e) {
                        null -> log.info { "Produced record to topic ${meta.topic()} partition [${meta.partition()}] @ offset ${meta.offset()}" }
                        else -> log.error(e) { "Error producing message to topic: $TOPIC_NAME" }
                    }
                }
            } catch (e: Exception) {
                // TODO: think about proper logging instead of rethrowing
                log.error(e) { "Exception while creating ${UserCreatedEvent::class.simpleName} message and producing to topic: $TOPIC_NAME" }
                throw e
            }
        }
    }
}
