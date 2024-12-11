package hu.bme.aut.transformapi.dto

data class TransformDto(
    val id: Long,
    val originPlatform: String,
    val destinationPlatform: String,
    val playlistName: String,
    val originPlaylistId: String,
    val destinationPlaylistId: String,
    val movesongEmail: String,
    val itemCount: Int,
    val thumbnailUrl: String,
    val date: String,
)
