package com.fszuberski.fooddelivery.common.plugins

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level

internal val log = KotlinLogging.logger {}

val HttpLogging = createApplicationPlugin("HttpLogging", ::HttpLoggingConfig) {
    val (
        logLevel,
        format,
        include
    ) = pluginConfig

    application.install(DoubleReceive)
    application.install(CallLogging) {
        level = logLevel
        format { call ->
            runBlocking { call.print(include, format) }
        }
    }

    on(CallFailed) { call, throwable ->
        log.info(throwable) { "HTTP call failed." }
    }
}

private suspend fun ApplicationCall.print(include: Include, format: Format) =
    buildString {
        val (
            fullUrl,
            queryParameters,
            requestHeaders,
            requestBody,
            responseHeaders,
            responseBody,
            elapsedTime
        ) = include

        if (format == Format.PRETTY) {
            appendLine(
                """
            Incoming HTTP Call:
            >>> Request
                Method:  ${request.httpMethod.value}
                URL:     ${if (fullUrl) request.url() else request.path()}
            """.trimIndent()
            )
            if (queryParameters) appendLine("\tQuery: ${request.queryString()}")
            if (requestHeaders) appendLine("\tHeaders: ${request.headers.humanReadable()}")
            if (requestBody) appendLine("\tBody:    ${receiveText()}")
            appendLine(
                """
            <<< Response
                Status: ${response.statusNotNull()}
            """.trimIndent()
            )
            if (responseHeaders) appendLine("\tHeaders: ${response.headers.allValues().humanReadable()}")
            if (responseBody) appendLine("\tBody: // TODO")  // TODO: response body
            if (elapsedTime) appendLine("\n\tElapsed time: ${processingTimeMillis()}ms")

        } else if (format == Format.CONCISE) {
            appendLine("Incoming HTTP request")
            append(">>> ${request.httpMethod.value} ${if (fullUrl) request.url() else request.path()}")
            if (queryParameters) request.queryString().let {
                if (it.isNotBlank()) append("?${request.queryString()}")
            }
            if (requestHeaders) append(" ${request.headers.humanReadable()}")
            if (requestBody) append(" ${receiveText()}")
            appendLine()
            append("<<< ${response.statusNotNull()}")
            if (responseHeaders) append(" ${response.headers.allValues().humanReadable()}")
            if (responseBody) append("")  // TODO: response body
            if (elapsedTime) append(" (${processingTimeMillis()}ms)")
        }
    }

data class HttpLoggingConfig(
    val level: Level = Level.INFO,
    val format: Format = Format.CONCISE,
    val include: Include = Include()
)

data class Include(
    val fullUrl: Boolean = false,
    val queryParameters: Boolean = true,
    val requestHeaders: Boolean = true,
    val requestBody: Boolean = true,
    val responseHeaders: Boolean = true,
    val responseBody: Boolean = true,
    val elapsedTime: Boolean = true
)

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
