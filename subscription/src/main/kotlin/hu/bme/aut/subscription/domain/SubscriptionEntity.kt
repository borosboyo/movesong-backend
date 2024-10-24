package hu.bme.aut.subscription.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "subscription",
)
open class SubscriptionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "userId", nullable = false)
    open var userId: Long,

    @Column(name = "customerId", nullable = false)
    open var customerId: String,

    @Column(name = "userEmail", nullable = false)
    open var userEmail: String,

    @Column(name = "username", nullable = false)
    open var username: String,

    @Column(name = "subscriptionId", nullable = false)
    open var subscriptionId: String,

    @Column(name = "productId", nullable = false)
    open var productId: String,

    @Column(name = "currentPeriodEnd", nullable = false)
    open var currentPeriodEnd: Long = 0,

    @Column(name = "price", nullable = false)
    open var price: Double = 0.0,

    @Column(name = "interval", nullable = false)
    open var interval: String = "",
    ) {

    constructor() : this(
        id = 0,
        userId = 0,
        customerId = "",
        userEmail = "",
        username = "",
        subscriptionId = "",
        productId = "",
        currentPeriodEnd = 0,
        price = 0.0,
        interval = "",
    )
}