package hu.bme.aut.userapi.dto.req

data class SaveForgotPasswordReq(
    val token: String,
    val newPassword: String
)