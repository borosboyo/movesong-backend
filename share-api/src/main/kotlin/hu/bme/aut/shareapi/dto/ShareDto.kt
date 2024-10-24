package hu.bme.aut.shareapi.dto

data class ShareDto(
    val id: Long ,
    val playlistId: String,
    val sharedPlaylistName: String,
    val ownerMovesongEmail: String,
    val visible: Boolean,
    val views: Int,
    val sharePlatformType: String,
    val selectedBackgroundId: Long,
    val thumbnailUrl: String,
)
