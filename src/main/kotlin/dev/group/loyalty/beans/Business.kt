package dev.group.loyalty.beans

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
@Entity
@Table(name = "businesses")
data class Business(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    val owner: User? = null,
    @ManyToMany
    @JoinTable(
        name = "business_workers",
        joinColumns = [JoinColumn(name = "business_id")],
        inverseJoinColumns = [JoinColumn(name = "worker_id")]
    )
    val workers: MutableList<User> = mutableListOf(),
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