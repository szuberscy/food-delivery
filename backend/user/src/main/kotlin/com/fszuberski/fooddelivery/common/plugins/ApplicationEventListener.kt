package com.fszuberski.fooddelivery.common.plugins

import io.ktor.server.application.*
import io.ktor.server.application.hooks.*

val ApplicationEventListener = createApplicationPlugin("ApplicationEventListener", ::ApplicationEventListenerConfig) {
    val (
        started,
        ready,
        stopPreparing,
        stopping,
        stopped
    ) = pluginConfig

    on(MonitoringEvent(ApplicationStarted)) { started() }
    on(MonitoringEvent(ServerReady)) { ready() }
    on(MonitoringEvent(ApplicationStopPreparing)) { stopPreparing() }
    on(MonitoringEvent(ApplicationStopping)) { stopping() }
    on(MonitoringEvent(ApplicationStopped)) { stopped() }
}

data class ApplicationEventListenerConfig(
    var started: () -> Unit = {},
    var ready: () -> Unit = {},
    var stopPreparing: () -> Unit = {},
    var stopping: () -> Unit = {},
    var stopped: () -> Unit = {}
)
