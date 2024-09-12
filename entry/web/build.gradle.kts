import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":api"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
