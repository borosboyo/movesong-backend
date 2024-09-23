package hu.bme.aut.userapi.dto.req

data class LoginReq(
    val usernameOrEmail: String,
    val password: String
)
