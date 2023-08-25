package com.fszuberski.fooddelivery.registration.adapter.kafka

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

// TODO: Move config to file
private const val KAFKA_SERVER_ADDRESS = "localhost:9092"
private const val SCHEMA_REGISTRY_SERVER_URL = "http://localhost:9081"
private val AVRO_SERIALIZER_STRING = StringSerializer::class.java
private val AVRO_SERIALIZER_CLASS = KafkaAvroSerializer::class.java

class Configuration {
    companion object {
        fun producer(): Producer<String, GenericRecord> {
           return KafkaProducer(properties())
        }

        private fun properties(): Properties {
            val kafkaProps = Properties()
            kafkaProps["bootstrap.servers"] = KAFKA_SERVER_ADDRESS
            kafkaProps["key.serializer"] = AVRO_SERIALIZER_STRING
            kafkaProps["value.serializer"] = AVRO_SERIALIZER_CLASS
            kafkaProps["schema.registry.url"] = SCHEMA_REGISTRY_SERVER_URL

            return kafkaProps
        }
    }
}