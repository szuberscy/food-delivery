package com.fszuberski.fooddelivery

import io.github.oshai.kotlinlogging.KotlinLogging
import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.configuration.ConfigUtils.LOCATIONS
import org.flywaydb.core.internal.configuration.ConfigUtils.TABLE

private val log = KotlinLogging.logger {}

fun main() {
    try {
        log.info { "Starting migration job" }
        val configuration = Configuration()

        log.info { "Reading external configuration" }
        val dbConfiguration = configuration.getConfiguration("db")

        dbConfiguration.getConfigurationMap()
            .forEach { (key, config) ->
                try {
                    // maintenance migrations are high-level migrations such as creating a database
                    val maintenanceConfig = config.getConfiguration("maintenance")
                    maintain(key, maintenanceConfig)

                    // regular migrations
                    val migrationConfig = config.getConfiguration("migration")
                    migrate(key, migrationConfig)
                } catch (e: Exception) {
                    log.error(e) { "Migration for $key failed" }
                }
            }

        log.info { "Migration job finished" }
    } catch (e: Exception) {
        log.error(e) { "Finished migration job unsuccessfully because of an unexpected exception" }
        throw e
    }
}

private fun maintain(key: String, config: Configuration) {
    val url = config.getString("url")
    val username = config.getString("username")
    val password = config.getString("password")

    Flyway.configure()
        .loggers("slf4j")
        .dataSource(url, username, password)
        .configuration(mapOf(
            LOCATIONS to "db/$key/maintenance",
            TABLE to "${key}__flyway_schema_history"
        ))
        .also { log.info { "Loading Flyway instance for $key" } }
        .load()
        .also {
            log.info { "Started applying maintenance migrations for $key" }
            it.migrate()
            log.info { "Applying maintenance migrations finished for $key" }
        }
}

private fun migrate(key: String, config: Configuration) {
    val url = config.getString("url")
    val username = config.getString("username")
    val password = config.getString("password")

    Flyway.configure()
        .loggers("slf4j")
        .dataSource(url, username, password)
        .configuration(mapOf(LOCATIONS to "db/$key/migration"))
        .also { log.info { "Loading Flyway instance for $key" } }
        .load()
        .also {
            log.info { "Started applying migrations for $key" }
            it.migrate()
            log.info { "Applying migrations finished for $key" }
        }
}
