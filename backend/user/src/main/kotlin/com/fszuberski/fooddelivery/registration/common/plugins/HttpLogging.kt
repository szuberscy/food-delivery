package com.fszuberski.fooddelivery.registration.common.plugins

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.slf4j.event.Level

internal val log = KotlinLogging.logger {}
internal val CALL_START_TIME = AttributeKey<Long>("CALL_START_TIME")

val HttpLogging = createApplicationPlugin("HttpLogging", ::HttpLoggingConfig) {
    val (
        logLevel, // TODO
        format,
        mode,
        include
    ) = pluginConfig

    application.install(DoubleReceive)

    on(CallFailed) { call, throwable ->
        log.info(throwable) { "HTTP call failed." }
    }

    on(CallSetup) { call ->
        call.attributes.put(CALL_START_TIME, System.currentTimeMillis())
    }

    onCall { call ->
        log.info { "OnCall" }
        when {
            mode == LogMode.SINGLE -> {
                log.info { 0 }
                return@onCall
            }
            format == Format.CONCISE -> {
                log.info { 1 }
                suspend {
                    call.printRequest(include)
                }()
            }

            format == Format.PRETTY -> {
                log.info { 2 }
                suspend {
                    call.prettyPrintRequest(include)
                }()
            }

        }
    }

    onCallReceive { call ->
    }

    onCallRespond { call, content ->
        log.info { "OnCallRespond" }
        val message = when {
            mode == LogMode.SINGLE && format == Format.CONCISE -> suspend {
                "${call.printRequest(include)} ${call.printResponse(include)}"
            }()

            mode == LogMode.SINGLE && format == Format.PRETTY -> suspend {
                buildString {
                    appendLine(call.prettyPrintRequest(include))
                    appendLine(call.prettyPrintResponse(include))
                }
            }()

            mode == LogMode.MULTIPLE && format == Format.CONCISE ->
                call.printResponse(include)

            mode == LogMode.MULTIPLE && format == Format.PRETTY ->
                call.prettyPrintResponse(include)

            else -> null
        }

        if (message != null) {
            log.info { message }
        }
    }
}

private suspend fun ApplicationCall.printRequest(include: Include) =
    buildString {
        val (
            queryParameters,
            requestHeaders,
            requestBody
        ) = include
        append(">>> ${request.httpMethod.value} ${request.url()}")
        if (queryParameters) append("?${request.queryString()}")
        if (requestHeaders) append(" ${request.headers.humanReadable()}")
        if (requestBody) append(" ${receiveText()}")
    }

private fun ApplicationCall.printResponse(include: Include) =
    buildString {
        val (
            _,
            _,
            _,
            responseHeaders,
            responseBody,
            elapsedTime
        ) = include
        append("<<< ${response.statusNotNull()}")
        if (responseHeaders) append(" ${response.headers.allValues().humanReadable()}")
        if (responseBody) append("")  // TODO: response body
        if (elapsedTime) append("(${elapsedTimeMillis()}ms)")
    }

private suspend fun ApplicationCall.prettyPrintRequest(include: Include) =
    buildString {
        val (
            queryParameters,
            requestHeaders,
            requestBody
        ) = include
        appendLine(
            """
            Incoming HTTP Call:
            >>> Request
                Method:  ${request.httpMethod.value}
                URL:     ${request.url()}
            """.trimIndent()
        )

        if (queryParameters) appendLine("\tQuery: ${request.queryString()}")
        if (requestHeaders) appendLine("\tHeaders: ${request.headers.humanReadable()}")
        if (requestBody) appendLine("\tBody:    ${receiveText()}")
    }

private fun ApplicationCall.prettyPrintResponse(include: Include) =
    buildString {
        val (
            _,
            _,
            _,
            responseHeaders,
            responseBody,
            elapsedTime
        ) = include

        appendLine(
            """
            <<< Response
                Status: ${response.statusNotNull()}
            """.trimIndent()
        )

        if (responseHeaders) appendLine("\tHeaders: ${response.headers.allValues().humanReadable()}")
        if (responseBody) appendLine("\tBody: // TODO")  // TODO: response body
        if (elapsedTime) appendLine("\n\tElapsed time: ${elapsedTimeMillis()}ms")
    }

data class HttpLoggingConfig(
    val level: Level = Level.INFO,
    val format: Format = Format.CONCISE,
    val mode: LogMode = LogMode.MULTIPLE,
    val include: Include = Include()
)

data class Include(
    val queryParameters: Boolean = true,
    val requestHeaders: Boolean = true,
    val requestBody: Boolean = true,
    val responseHeaders: Boolean = true,
    val responseBody: Boolean = true,
    val elapsedTime: Boolean = true
)

internal fun ApplicationCall.elapsedTimeMillis(): Long =
    System.currentTimeMillis() - attributes[CALL_START_TIME]

internal fun ApplicationRequest.url() =
    "${call.request.local.scheme}://${call.request.local.serverHost}:${call.request.local.localPort}${call.request.path()}"

internal fun ApplicationResponse.statusNotNull() =
    call.response.status() ?: HttpStatusCode.InternalServerError

internal fun Headers.humanReadable() =
    entries().joinToString {
        "${it.key}: ${it.value}"
    }

enum class Format {
    PRETTY, CONCISE
}

enum class LogMode {
    SINGLE, MULTIPLE
}