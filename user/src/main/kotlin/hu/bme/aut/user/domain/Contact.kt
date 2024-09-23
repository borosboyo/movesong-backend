package hu.bme.aut.user.domain

import jakarta.persistence.*

@Entity(name = "contact")
open class Contact(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "subject", nullable = false)
    open var subject: String,

    @Column(name = "name", nullable = false)
    open var name: String,

    @Column(name = "email", nullable = false)
    open var email: String,

    @Column(name = "message", nullable = false)
    open var message: String
) {
    constructor() : this(0, "", "", "", "")
}