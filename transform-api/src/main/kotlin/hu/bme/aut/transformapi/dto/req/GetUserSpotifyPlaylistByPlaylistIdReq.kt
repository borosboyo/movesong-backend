package hu.bme.aut.transformapi.dto.req

data class GetUserSpotifyPlaylistByPlaylistIdReq(
    val movesongEmail: String,
    val playlistId: String,
)
