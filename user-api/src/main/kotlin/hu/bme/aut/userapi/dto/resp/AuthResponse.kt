package hu.bme.aut.userapi.dto.resp

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)