package me.soo.helloworld

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class HelloworldApplication

fun main(args: Array<String>) {
    runApplication<HelloworldApplication>(*args)
}
