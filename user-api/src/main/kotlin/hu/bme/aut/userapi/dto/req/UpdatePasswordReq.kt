package hu.bme.aut.userapi.dto.req

data class UpdatePasswordReq(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)