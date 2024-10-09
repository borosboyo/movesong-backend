package hu.bme.aut.transformapi.dto

data class ConnectionDto(
    val movesongEmail: String,
    val connectionType: String,
    val accessToken: String,
    val refreshToken: String,
)
