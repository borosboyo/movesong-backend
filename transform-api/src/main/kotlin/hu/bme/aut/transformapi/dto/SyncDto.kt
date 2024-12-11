package hu.bme.aut.transformapi.dto

data class SyncDto(
    val id: Long,
    val enabled: Boolean,
    val playlistName: String,
    val youtubePlaylistId: String,
    val spotifyPlaylistId: String,
    val movesongEmail: String,
    val date: String,
    val lastSyncDate: String,
    val interval: Int
)
