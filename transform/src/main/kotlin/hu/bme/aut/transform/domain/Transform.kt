package hu.bme.aut.transform.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "transform"
)
open class Transform(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,
) {
    constructor() : this(
        0
    )
}