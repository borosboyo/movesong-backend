package hu.bme.aut.transformapi.dto

data class PlaylistDto(
    val id: String,
    val title: String,
    val channelTitle: String,
    val thumbnailUrl: String,
    val itemCount: Long,
)
