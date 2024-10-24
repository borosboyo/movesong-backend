package hu.bme.aut.userapi.dto.req

data class CheckForgotPasswordTokenReq(
    val email: String,
    val token: String
)
