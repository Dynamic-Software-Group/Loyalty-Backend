package dev.group.loyalty.auth

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class CredHandler {
    private val encoder = BCryptPasswordEncoder()
    fun hashPassword(password: String): String {
        return encoder.encode(password)
    }

    fun checkPassword(password: String, hash: String): Boolean {
        return encoder.matches(password, hash)
    }
}