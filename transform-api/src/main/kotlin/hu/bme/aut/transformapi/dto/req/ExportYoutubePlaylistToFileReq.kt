package hu.bme.aut.transformapi.dto.req

data class ExportYoutubePlaylistToFileReq(
    val movesongEmail: String,
    val playlistId: String,
)
