package dev.group.loyalty.logic

import dev.group.loyalty.auth.CredHandler
import dev.group.loyalty.auth.JwtUtils
import dev.group.loyalty.endpoints.UserEndpoints
import dev.group.loyalty.utils.RedisRepository
import dev.group.loyalty.utils.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import dev.group.loyalty.beans.User as UserBean

@Component
class User(
    private val userRepo: UserRepository, private val redisRepo: RedisRepository
) {
    private val jwt = JwtUtils()

    private val credHandler = CredHandler()
    fun createUser(request: UserEndpoints.UserCreateRequest): UserBean {
        val user = UserBean(
            name = request.name,
            email = request.email,
            passwordHash = credHandler.hashPassword(request.password)
        )
        userRepo.save(user)
        return user
    }

    fun getUser(id: Int): UserBean {
        return userRepo.findById(id).get()
    }

    fun login(request: UserEndpoints.LoginRequest): String? {
        val user = getUserByEmail(request.email) ?: return null
        if (credHandler.checkPassword(request.password, user.passwordHash)) {
            val jwt: String = jwt.encode(user)
            redisRepo.save(jwt)
            return jwt
        }
        return null
    }

    fun logout(jwt: String) {
        redisRepo.delete(jwt)
    }

    fun getUserByEmail(email: String): UserBean? {
        for (user in userRepo.findAll()) {
            if (user.email == email) {
                return user
            }
        }
        return null;
    }
}