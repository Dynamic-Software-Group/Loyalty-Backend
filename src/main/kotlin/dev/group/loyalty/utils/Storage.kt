package dev.group.loyalty.utils

import dev.group.loyalty.beans.Business
import dev.group.loyalty.beans.User
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Int>

@Repository
interface BusinessRepository: CrudRepository<Business, Int>

@Repository
class RedisRepository(private val redisTemplate: RedisTemplate<String, String>) {
    fun save(jwt: String) {
        redisTemplate.opsForList().rightPush("jwts", jwt)
    }

    fun valid(jwt: String): Boolean {
        val index = redisTemplate.opsForList().indexOf("jwts", jwt)
        return index != -1L
    }

    fun delete(jwt: String) {
        redisTemplate.opsForList().remove("jwts", 0, jwt)
    }
}
