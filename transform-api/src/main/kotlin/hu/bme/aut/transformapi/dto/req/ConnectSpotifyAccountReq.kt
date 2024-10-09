package hu.bme.aut.transformapi.dto.req

data class ConnectSpotifyAccountReq(
    val code: String,
    val state: String,
    val movesongEmail: String
)
