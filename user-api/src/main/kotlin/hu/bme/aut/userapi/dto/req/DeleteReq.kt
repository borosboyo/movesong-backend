package hu.bme.aut.userapi.dto.req

data class DeleteReq(
    val id: Long,
    val email: String,
)