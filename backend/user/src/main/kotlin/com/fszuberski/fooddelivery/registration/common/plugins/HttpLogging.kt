package com.fszuberski.fooddelivery.registration.common.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level

val HttpLogging = createApplicationPlugin("HttpLogging", ::HttpLoggingConfig) {
    val (
        logLevel,
        queryParameters,
        requestHeaders,
        requestBody,
        responseHeaders,
        responseBody,
        elapsedTime
    ) = pluginConfig

    application.install(DoubleReceive)
    application.install(CallLogging) {
        level = logLevel
        format { call ->
            runBlocking {
                // TODO: pass pretty printing as an option
                call.prettyPrint(
                    queryParameters,
                    requestHeaders,
                    requestBody,
                    responseHeaders,
                    responseBody,
                    elapsedTime
                )
            }
        }
    }
}

private suspend fun ApplicationCall.prettyPrint(
    queryParameters: Boolean,
    requestHeaders: Boolean,
    requestBody: Boolean,
    responseHeaders: Boolean,
    responseBody: Boolean,
    elapsedTime: Boolean,
): String {
    return buildString {
        appendLine(
            """
                        HTTP Call:
                        >>> Request
                            Method:  ${request.httpMethod.value}
                            URL:     ${request.url()}
                        """
                .trimIndent()
        )

        if (queryParameters) {
            appendLine("\tQuery: ${request.queryString()}")
        }

        if (requestHeaders) {
            appendLine("\tHeaders: ${request.headers.humanReadable()}")
        }

        if (requestBody) {
            appendLine("\tBody:    ${receiveText()}")
        }

        appendLine(
            """
                        <<< Response
                            Status: ${response.status()}
                    """.trimIndent()
        )

        if (responseHeaders) {
            appendLine("\tHeaders: ${response.headers.allValues().humanReadable()}")
        }

        // TODO: response body
        if (responseBody) {
            appendLine("\tBody: // TODO")
        }

        if (elapsedTime) {
            appendLine("\n\tElapsed time: ${processingTimeMillis()}ms")
        }

    }
}

data class HttpLoggingConfig(
    val level: Level = Level.INFO,
    val queryParameters: Boolean = true,
    val requestHeaders: Boolean = true,
    val requestBody: Boolean = true,
    val responseHeaders: Boolean = true,
    val responseBody: Boolean = true,
    val elapsedTime: Boolean = true
)

/*
fun Headers.humanReadable(pretty: Boolean = false): String {
    return StringBuilder().let { sb ->
        this.entries().forEach { entry ->
            sb.append("${entry.key}: ${entry.value}")
        }
    }.toString()
}
*/

fun ApplicationRequest.url() =
    "${call.request.local.scheme}://${call.request.local.serverHost}:${call.request.local.localPort}${call.request.path()}"

fun Headers.humanReadable() =
    entries().joinToString {
        "${it.key}: ${it.value}"
    }
