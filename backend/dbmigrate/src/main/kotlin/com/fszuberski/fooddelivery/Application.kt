package com.fszuberski.fooddelivery

import io.github.oshai.kotlinlogging.KotlinLogging
import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.configuration.ConfigUtils

private val log = KotlinLogging.logger {}

fun main() {
    try {
        log.info { "Starting migration job" }
        val configuration = Configuration()

        log.info { "Reading external configuration" }
        val dbConfiguration = configuration.getConfiguration("db.migration")

        dbConfiguration.getConfigurationMap()
            .forEach { (key, config) -> migrate(key, config) }

        log.info { "Migration job finished" }
    } catch (e: Exception) {
        log.error(e) { "Finished migration job unsuccessfully because of an unexpected exception" }
        throw e
    }
}

private fun migrate(key: String, config: Configuration) {
    try {
        log.info { "Configuring Flyway for $key" }
        val url = config.getString("url")
        val username = config.getString("username")
        val password = config.getString("password")

        Flyway.configure()
            .dataSource(url, username, password)
            .configuration(mapOf(ConfigUtils.LOCATIONS to "db/migration/$key"))
            .also { log.info { "Loading Flyway instance for $key" } }
            .load()
            .also {
                log.info { "Started applying migrations for $key" }
                it.migrate()
                log.info { "Applying migrations finished for $key" }
            }
    } catch (e: Exception) {
        log.error(e) { "Migration for $key failed" }
    }
}
