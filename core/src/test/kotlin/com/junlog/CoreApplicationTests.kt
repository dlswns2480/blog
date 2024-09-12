package com.junlog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(TestcontainersConfiguration::class)
class InfraApplicationTests

fun main(args: Array<String>) {
    runApplication<InfraApplicationTests>(*args)
}
