package dev.group.loyalty

import dev.group.loyalty.endpoints.BusinessEndpoints
import dev.group.loyalty.endpoints.UserEndpoints
import dev.group.loyalty.utils.BusinessRepository
import dev.group.loyalty.utils.RedisRepository
import dev.group.loyalty.utils.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackageClasses = [UserEndpoints::class, BusinessEndpoints::class, RedisRepository::class])
@EnableJpaRepositories(basePackageClasses = [UserRepository::class, BusinessRepository::class])
class LoyaltyApplication

fun main(args: Array<String>) {
    runApplication<LoyaltyApplication>(*args)
}