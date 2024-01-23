package dev.group.loyalty.logic

import dev.group.loyalty.auth.JwtUtils
import dev.group.loyalty.beans.Business as BusinessBean
import dev.group.loyalty.endpoints.BusinessEndpoints
import dev.group.loyalty.utils.BusinessRepository
import dev.group.loyalty.utils.RedisRepository
import dev.group.loyalty.utils.UserRepository
import org.springframework.stereotype.Component

@Component
class Business(private val businessRepo: BusinessRepository, private val userRepo: UserRepository, private val redisRepo: RedisRepository) {
    private val jwt = JwtUtils()
    fun createBusiness(jwt: String, request: BusinessEndpoints.BusinessCreateRequest): BusinessBean?  {
        val user = this.jwt.decode(jwt, User(userRepo, redisRepo))
        val business = BusinessBean(
            owner = user,
            name = request.name,
            description = request.description,
            icon = request.icon,
            address = request.address,
            city = request.city,
            state = request.state,
            zip = request.zip
        )
        businessRepo.save(business)
        return business
    }

    fun deleteBusiness(jwt: String) {
        val user = this.jwt.decode(jwt, User(userRepo, redisRepo))
        val business = businessRepo.findAll().find { it.owner == user } ?: return
        businessRepo.delete(business)
    }
}