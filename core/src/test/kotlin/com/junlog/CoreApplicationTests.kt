package com.junlog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class InfraApplicationTests

fun main(args: Array<String>) {
    runApplication<InfraApplicationTests>(*args)
}
