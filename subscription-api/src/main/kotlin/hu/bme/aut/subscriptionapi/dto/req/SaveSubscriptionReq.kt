package hu.bme.aut.subscriptionapi.dto.req

data class SaveSubscriptionReq(
    val userId: Long,
    val userEmail: String,
    val username: String,
    val customerId: String,
)
