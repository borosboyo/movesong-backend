package hu.bme.aut.subscriptionapi.dto.resp

data class FindSubscriptionResp(
    val userId: Long,
    val customerId: String,
    val userEmail: String,
    val username: String,
    val subscriptionId: String,
    val productId: String,
    val currentPeriodEnd: Long,
    val price: Double,
    val interval: String,
)
