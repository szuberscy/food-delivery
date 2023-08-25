package com.fszuberski.fooddelivery.registration.adapter.kafka

import com.fszuberski.fooddelivery.registration.core.domain.User
import com.fszuberski.fooddelivery.user.UserCreatedEvent
import com.fszuberski.fooddelivery.registration.port.out.ProduceUserCreatedPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata

private val log = KotlinLogging.logger {}
private const val TOPIC_NAME = "user-events"

class UserEventsProducer : ProduceUserCreatedPort {
    override fun produceUserCreatedEvent(user: User) {
        // TODO: immediately closes the producer once a message is produced.
        // Could be cached so we don't reestablish connections.
        Configuration.producer().use { producer ->
            try {
                val userCreatedEvent = UserCreatedEvent(user.id.toString(), user.name, user.surname)
                producer.send(
                    ProducerRecord(TOPIC_NAME, user.id.toString(), userCreatedEvent)
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
