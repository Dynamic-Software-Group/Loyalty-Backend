package dev.group.loyalty.auth

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWT
import dev.group.loyalty.beans.User as UserBean
import dev.group.loyalty.logic.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class JwtUtils {
    private val SECRET: String = "secret"

    fun encode(user: UserBean): String {
        try {
            val algorithm = Algorithm.HMAC256(SECRET)
            val token: String = JWT.create()
                .withIssuer("backend")
                .withClaim("id", user.id)
                .sign(algorithm)
            return token
        } catch (e: Exception) {
            throw RuntimeException(e)

        }
    }

    fun decode(jwt: String, user: User): UserBean {
        try {
            val algorithm = Algorithm.HMAC256(SECRET)
            val verifier = JWT.require(algorithm)
                .withIssuer("backend")
                .build()
            val decodedJWT = verifier.verify(jwt)
            val id = decodedJWT.getClaim("id").asInt()
            return user.getUser(id)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}