package hu.bme.aut.userapi.dto.req

data class DeleteReq(
    val email: String,
    val subscriptionId: String? = null,
)