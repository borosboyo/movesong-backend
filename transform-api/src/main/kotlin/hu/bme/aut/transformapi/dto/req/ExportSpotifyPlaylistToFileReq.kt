package hu.bme.aut.transformapi.dto.req

data class ExportSpotifyPlaylistToFileReq(
    val movesongEmail: String,
    val playlistId: String,
)
