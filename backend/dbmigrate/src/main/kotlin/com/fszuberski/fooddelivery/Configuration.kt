package com.fszuberski.fooddelivery

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

// TODO: move to common library

class Configuration(private val config: Config) {

    constructor(fileName: String = "application") : this(
        ConfigFactory.load(fileName)
    )

    fun getString(key: String) = config.getString(key)

    fun getBoolean(key: String) = config.getBoolean(key)

    fun getInt(key: String) = config.getInt(key)

    fun getLong(key: String) = config.getLong(key)


    fun getConfiguration(key: String) =
        Configuration(config.getConfig(key))

    fun getConfigurationList(key: String) =
        config.getConfigList(key).map { Configuration(it) }

    fun getConfigurationMap() = config.root()
        .entries
        .associate { it.key to getConfiguration(it.key) }
}