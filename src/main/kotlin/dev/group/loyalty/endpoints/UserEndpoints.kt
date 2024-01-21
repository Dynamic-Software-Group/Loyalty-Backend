package dev.group.loyalty.endpoints

import dev.group.loyalty.auth.JwtUtils
import dev.group.loyalty.beans.Points
import dev.group.loyalty.logic.User
import dev.group.loyalty.utils.BusinessRepository
import dev.group.loyalty.utils.RedisRepository
import dev.group.loyalty.utils.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import dev.group.loyalty.beans.User as UserBean

@RestController
@RequestMapping("/users")
class UserEndpoints @Autowired constructor(
    private val userRepo: UserRepository,
    private val redisRepo: RedisRepository,
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

    @PostMapping("/points/add/{id}")
    fun addPoints(jwt: String, points: Int, @PathVariable id: String): ResponseEntity<Unit> {
        if (!redisRepo.valid(jwt)) return ResponseEntity.badRequest().build()
        val user = this.jwt.decode(jwt, User(userRepo, redisRepo))
        this.user.addPoints(user, points, id.toInt())
        return ResponseEntity.ok().build()
    }

    @GetMapping("/points/{id}")
    fun getPoints(jwt: String, @PathVariable id: String): ResponseEntity<Int> {
        if (!redisRepo.valid(jwt)) return ResponseEntity.badRequest().build()
        val user = this.jwt.decode(jwt, User(userRepo, redisRepo))
        val points = user.points.find { it.businessId == id.toInt() }?.points ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(points)
    }

    @DeleteMapping("/points/delete/{id}")
    fun deletePoints(jwt: String, @PathVariable id: String): ResponseEntity<Unit> {
        if (!redisRepo.valid(jwt)) return ResponseEntity.badRequest().build()
        val user = this.jwt.decode(jwt, User(userRepo, redisRepo))
        val points = user.points.find { it.businessId == id.toInt() } ?: return ResponseEntity.badRequest().build()
        user.points.remove(points)
        userRepo.save(user)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/points/set/{id}")
    fun setPoints(jwt: String, points: Int, @PathVariable id: String): ResponseEntity<Unit> {
        if (!redisRepo.valid(jwt)) return ResponseEntity.badRequest().build()
        val user = this.jwt.decode(jwt, User(userRepo, redisRepo))
        val existingPoints = user.points.find { it.businessId == id.toInt() }
        if (existingPoints != null) {
            existingPoints.points = points
        } else {
            val newPoints = Points(businessId = id.toInt(), points = points)
            user.points.add(newPoints)
        }
        userRepo.save(user)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/points/remove/{id}")
    fun removePoints(jwt: String, points: Int, @PathVariable id: String): ResponseEntity<Unit> {
        if (!redisRepo.valid(jwt)) return ResponseEntity.badRequest().build()
        val user = this.jwt.decode(jwt, User(userRepo, redisRepo))
        val existingPoints = user.points.find { it.businessId == id.toInt() }
        if (existingPoints != null) {
            existingPoints.points -= points
        } else {
            val newPoints = Points(businessId = id.toInt(), points = -points)
            user.points.add(newPoints)
        }
        userRepo.save(user)
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