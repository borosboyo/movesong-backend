package hu.bme.aut.userapi.dto.req

data class UpdatePasswordReq(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)