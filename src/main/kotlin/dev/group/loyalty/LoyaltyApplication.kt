package dev.group.loyalty

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoyaltyApplication

fun main(args: Array<String>) {
    runApplication<LoyaltyApplication>(*args)
}
