package dev.group.loyalty.beans

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
    val businesses: List<Business> = listOf(),
    @ManyToMany(mappedBy = "workers")
    val workingForBusinesses: List<Business> = listOf(),
    @Column(name = "num_of_businesses_works_for")
    val numOfBusinessesWorksFor: Int = 0,
    @Column(name = "num_points")
    val numPoints: Int = 0,
    @Column(name = "num_points_redeemed")
    val numPointsRedeemed: Int = 0
)

enum class Role {
    MEMBER, ADMIN
}