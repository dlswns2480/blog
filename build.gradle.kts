import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}

subprojects {
    apply(plugin = "kotlin")

    group = "com.junlog"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        // kotest
        testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.1")
        testImplementation("io.kotest:kotest-assertions-core-jvm:5.8.1")

        // logging
        implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}