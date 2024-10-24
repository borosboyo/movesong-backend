package hu.bme.aut.subscriptionapi.dto.req

data class SubscriptionReq(
    val email: String,
    val username: String,
    val interval: String,
    val productId: String
)