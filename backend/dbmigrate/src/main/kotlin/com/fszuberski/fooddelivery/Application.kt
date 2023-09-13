package com.fszuberski.fooddelivery

import io.github.oshai.kotlinlogging.KotlinLogging
import org.flywaydb.core.Flyway

private val log = KotlinLogging.logger {}

fun main() {
    try {
        log.info { "Starting migration job" }
        val configuration = Configuration()

        log.info { "Reading external configuration" }
        // TODO: support multiple databases, keep configuration as a map
        val dbConfiguration = configuration.getConfiguration("db")
        val url = dbConfiguration.getString("url")
        val username = dbConfiguration.getString("username")
        val password = dbConfiguration.getString("password")

        log.info { "Configuring Flyway" }
        Flyway.configure()
            .dataSource(url, username, password)
            .also { log.info { "Loading Flyway instance" } }
            .load()
            .also {
                log.info { "Started applying migrations" }
                it.migrate()
                log.info { "Applying migrations finished" }
            }
        log.info { "Finished migration job successfully" }
    } catch (e: Exception) {
        log.error(e) { "Finished migration job unsuccessfully because of an unexpected exception" }
        throw e
    }
}
