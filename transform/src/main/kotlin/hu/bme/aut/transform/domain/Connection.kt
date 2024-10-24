package hu.bme.aut.transform.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "connection"
)
open class Connection(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "movesong_email", nullable = false)
    open var movesongEmail: String,

    @Column(name = "platform_type", nullable = false)
    @Enumerated(EnumType.STRING)
    open var platformType: PlatformType,

    @Column(name = "access_token", nullable = false, length = 1024)
    open var accessToken: String,

    @Column(name = "refresh_token", nullable = false, length = 1024)
    open var refreshToken: String,
) {
    constructor() : this(
        0,
        "",
        PlatformType.YOUTUBE,
        "",
        ""
    )
}