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

    @Column(name = "connection_type", nullable = false)
    @Enumerated(EnumType.STRING)
    open var connectionType: ConnectionType,

    @Column(name = "access_token", nullable = false, length = 1024)
    open var accessToken: String,

    @Column(name = "refresh_token", nullable = false, length = 1024)
    open var refreshToken: String,
) {
    constructor() : this(
        0,
        "",
        ConnectionType.YOUTUBE,
        "",
        ""
    )
}