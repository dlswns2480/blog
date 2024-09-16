package com.junlog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ApiApplicationTests

fun main(args: Array<String>) {
    runApplication<ApiApplicationTests>(*args)
}
