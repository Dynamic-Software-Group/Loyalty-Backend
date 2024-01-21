package dev.group.loyalty.endpoints

import dev.group.loyalty.logic.Business
import dev.group.loyalty.beans.Business as BusinessBean
import dev.group.loyalty.utils.BusinessRepository
import dev.group.loyalty.utils.RedisRepository
import dev.group.loyalty.utils.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/business")
class BusinessEndpoints @Autowired constructor(
    private val businessRepo: BusinessRepository,
    private val userRepo: UserRepository,
    private val redisRepo: RedisRepository
){
    private val business = Business(businessRepo, userRepo, redisRepo)

    @PostMapping("/create")
    fun createBusiness(@RequestHeader(HttpHeaders.AUTHORIZATION) jwt: String, request: BusinessCreateRequest): ResponseEntity<BusinessBean> {
        if (!redisRepo.valid(jwt)) return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(business.createBusiness(jwt, request) ?: return ResponseEntity.badRequest().build())
    }

    @DeleteMapping("/delete")
    fun deleteBusiness(@RequestHeader(HttpHeaders.AUTHORIZATION) jwt: String): ResponseEntity<Unit> {
        if (!redisRepo.valid(jwt)) return ResponseEntity.badRequest().build()
        business.deleteBusiness(jwt)
        return ResponseEntity.ok().build()
    }

    data class BusinessCreateRequest(
        val name: String,
        val description: String,
        val icon: String, // url
        val address: String, // street number and name
        val city: String,
        val state: String,
        val zip: String
    )
}