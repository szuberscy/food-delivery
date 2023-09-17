package com.fszuberski.fooddelivery.common.persistence

import com.fszuberski.fooddelivery.common.configuration.DatabaseConfiguration
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object HikariProvider {

    fun getDataSource(dbConfiguration: DatabaseConfiguration) =
        getDataSource(getConfiguration(dbConfiguration))

    fun getDataSource(hikariConfig: HikariConfig) =
        HikariDataSource(hikariConfig)

    fun getConfiguration(dbConfiguration: DatabaseConfiguration) =
        HikariConfig().apply {
            jdbcUrl = dbConfiguration.url
            username = dbConfiguration.username
            password = dbConfiguration.password
        }
}