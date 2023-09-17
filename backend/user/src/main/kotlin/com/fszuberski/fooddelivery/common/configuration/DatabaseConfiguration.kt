package com.fszuberski.fooddelivery.common.configuration

// TODO: move to common library

class DatabaseConfiguration(
    configuration: Configuration = Configuration().getConfiguration("db")
) {
    val url: String
    val username: String
    val password: String

    init {
        url = configuration.getString("url")
        username = configuration.getString("username")
        password = configuration.getString("password")
    }
}