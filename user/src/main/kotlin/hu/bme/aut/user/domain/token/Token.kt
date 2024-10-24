package hu.bme.aut.user.domain.token

import jakarta.persistence.*

@Entity
@Table(name = "token")
open class Token(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "token", nullable = false)
    open var token: String,

    @Column(name = "token_type", nullable = false)
    @Enumerated(EnumType.STRING)
    open var tokenType: TokenType,

    @Column(name = "revoked", nullable = false)
    open var revoked: Boolean,

    @Column(name = "expired", nullable = false)
    open var expired: Boolean,

    @Column(name = "user_id", nullable = false)
    open var userId: Long
) {
    constructor() : this(0, "", TokenType.BEARER, false, false, 0)
}