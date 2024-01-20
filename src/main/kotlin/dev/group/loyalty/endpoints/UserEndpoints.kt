package dev.group.loyalty.endpoints

import dev.group.loyalty.auth.JwtUtils
import dev.group.loyalty.logic.User
import dev.group.loyalty.utils.BusinessRepository
import dev.group.loyalty.utils.RedisRepository
import dev.group.loyalty.utils.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import dev.group.loyalty.beans.User as UserBean

@RestController
@RequestMapping("/users")
class UserEndpoints @Autowired constructor(
    private val userRepo: UserRepository,
    private val redisRepo: RedisRepository
) {
    private val user = User(userRepo, redisRepo)
    private val jwt = JwtUtils()

    @PostMapping("/register")
    fun createUser(userReq: UserCreateRequest): ResponseEntity<UserBean> {
        println("Creating user")
        return ResponseEntity.ok(user.createUser(userReq))
    }

    @PostMapping("/login")
    fun login(request: LoginRequest): ResponseEntity<LoginResponse> {
        val jwt = user.login(request) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(LoginResponse(jwt))
    }

    @PostMapping("/logout")
    fun logout(jwt: String): ResponseEntity<Unit> {
        user.logout(jwt)
        return ResponseEntity.ok().build()
    }

    data class UserCreateRequest(
        val name: String,
        val email: String,
        val password: String
    )

    data class LoginRequest(
        val email: String,
        val password: String
    )

    data class LoginResponse(
        val jwt: String
    )
}