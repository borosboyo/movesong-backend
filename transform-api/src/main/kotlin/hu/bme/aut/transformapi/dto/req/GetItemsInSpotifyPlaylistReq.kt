package hu.bme.aut.transformapi.dto.req

data class GetItemsInSpotifyPlaylistReq(
    val playlistId: String,
    val movesongEmail: String
)
