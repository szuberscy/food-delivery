val kotlin_version: String by project
val postgres_jdbc_driver_version: String by project
val flyway_version: String by project
val typesafe_config_version: String by project
val kotlinlogging_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.9.10"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.fszuberski.fooddelivery.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Duser.timezone=UTC")
    applicationName = "dbmigrate"
}

group = "com.fszuberski.fooddelivery"
version = "0.0.1"

dependencies {
    implementation("org.postgresql:postgresql:$postgres_jdbc_driver_version")
    implementation("org.flywaydb:flyway-core:$flyway_version")
    implementation("com.typesafe:config:$typesafe_config_version")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinlogging_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}

tasks.jar { archiveBaseName.set(application.applicationName) }
