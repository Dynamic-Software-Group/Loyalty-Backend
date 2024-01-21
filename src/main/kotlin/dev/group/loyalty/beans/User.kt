package dev.group.loyalty.beans

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*;

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(name = "name")
    val name: String = "",
    @Column(name = "email")
    val email: String = "",
    @Column(name = "password_hash")
    val passwordHash: String = "",
    @Column(name = "role")
    val role: Role = Role.MEMBER,
    @Column(name = "businesses_owned")
    val ownsBusiness: Int = 0,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "owner_id")
    @JsonManagedReference
    val businesses: MutableList<Business> = mutableListOf(),
    @ManyToMany(mappedBy = "workers")
    val workingForBusinesses: MutableList<Business> = mutableListOf(),
    @Column(name = "num_of_businesses_works_for")
    val numOfBusinessesWorksFor: Int = 0,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @Column(name = "points")
    var points: MutableList<Points> = mutableListOf()
)

enum class Role {
    MEMBER, ADMIN
}