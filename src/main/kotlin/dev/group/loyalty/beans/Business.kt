package dev.group.loyalty.beans

import jakarta.persistence.*

@Entity
@Table(name = "businesses")
data class Business(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val owner: User? = null,
    @ManyToMany
    @JoinTable(
        name = "business_workers",
        joinColumns = [JoinColumn(name = "business_id")],
        inverseJoinColumns = [JoinColumn(name = "worker_id")]
    )
    val workers: List<User> = listOf(),
    @Column(name = "name")
    val name: String = "",
    @Column(name = "description")
    val description: String = "",
    @Column(name = "icon")
    val icon: String = "", // url
    @Column(name = "address")
    val address: String = "", // street number and name
    @Column(name = "city")
    val city: String = "",
    @Column(name = "state")
    val state: String = "",
    @Column(name = "zip")
    val zip: String = "",
)