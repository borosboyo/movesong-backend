package hu.bme.aut.userapi.dto.req

data class AuthRequest(
    val email: String,
    val password: String,
    val name: String
)