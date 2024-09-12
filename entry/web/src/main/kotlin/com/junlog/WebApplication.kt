package com.junlog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class WebApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application-core")
    runApplication<WebApplication>(*args)
}
