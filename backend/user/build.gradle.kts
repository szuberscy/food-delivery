import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val junit_version: String by project
val arrow_version: String by project
val avro_version: String by project
val avro_serializer_version: String by project
val kafka_version: String by project
val postgres_jdbc_driver_version: String by project
val kotlinlogging_version: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.8.0"
}

group = "com.fszuberski"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

val generateAvro = tasks.register<GenerateAvroJavaTask>("generateAvro") {
    setSource("../avro")
    setOutputDir(file("build/generated-main-avro-java"))
}

tasks.named("compileKotlin") {
    dependsOn(generateAvro)
}

sourceSets {
    main {
        java {
            srcDir("build/generated-main-avro-java")
        }
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-double-receive")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinlogging_version")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("org.postgresql:postgresql:$postgres_jdbc_driver_version")
    implementation("org.jdbi:jdbi3-core:3.41.1")
    implementation("org.jdbi:jdbi3-kotlin:3.41.1")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("io.arrow-kt:arrow-core:$arrow_version")
    implementation("io.arrow-kt:arrow-fx-coroutines:$arrow_version")
    implementation("commons-validator:commons-validator:1.7")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.apache.kafka:kafka-clients:$kafka_version")
    implementation("org.apache.avro:avro:$avro_version")
    implementation("io.confluent:kafka-avro-serializer:${avro_serializer_version}")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.3")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.3")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junit_version")
}
