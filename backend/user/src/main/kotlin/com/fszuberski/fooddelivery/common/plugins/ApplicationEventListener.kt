package com.fszuberski.fooddelivery.common.plugins

import io.ktor.server.application.*
import io.ktor.server.application.hooks.*

val ApplicationEventListener = createApplicationPlugin("ApplicationEventListener", ::ApplicationEventListenerConfig) {
    val (
        starting,
        started,
        ready,
        stopPreparing,
        stopping,
        stopped
    ) = pluginConfig

    on(MonitoringEvent(ApplicationStarting)) { starting() }
    on(MonitoringEvent(ApplicationStarted)) { started() }
    on(MonitoringEvent(ServerReady)) { ready() }
    on(MonitoringEvent(ApplicationStopPreparing)) { stopPreparing() }
    on(MonitoringEvent(ApplicationStopping)) { stopping() }
    on(MonitoringEvent(ApplicationStopped)) { stopped() }
}

data class ApplicationEventListenerConfig(
    val starting: () -> Unit = {},
    val started: () -> Unit = {},
    val ready: () -> Unit = {},
    val stopPreparing: () -> Unit = {},
    val stopping: () -> Unit = {},
    val stopped: () -> Unit = {}
)
