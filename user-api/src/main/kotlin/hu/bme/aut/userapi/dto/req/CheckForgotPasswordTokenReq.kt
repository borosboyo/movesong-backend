package hu.bme.aut.userapi.dto.req

data class CheckForgotPasswordTokenReq(
    val username: String,
    val token: String
)