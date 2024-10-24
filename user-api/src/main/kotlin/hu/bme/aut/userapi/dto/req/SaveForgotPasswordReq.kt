package hu.bme.aut.userapi.dto.req

data class SaveForgotPasswordReq(
    val email: String,
    val newPassword: String
)