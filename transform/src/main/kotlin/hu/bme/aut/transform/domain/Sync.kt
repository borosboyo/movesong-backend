package hu.bme.aut.transform.domain

import hu.bme.aut.transformapi.dto.SyncDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "Sync"
)
open class Sync(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "enabled", nullable = false)
    open var enabled: Boolean = true,

    @Column(name = "playlist_name", nullable = false)
    open var playlistName: String,

    @Column(name = "youtube_playlist_id", nullable = false)
    open var youtubePlaylistId: String,

    @Column(name = "spotify_playlist_id", nullable = false)
    open var spotifyPlaylistId: String,

    @Column(name = "movesong_email", nullable = false)
    open var movesongEmail: String,

    @Column(name = "date", nullable = false)
    open var creationDate: LocalDateTime,

    @Column(name = "last_sync_date", nullable = true)
    open var lastSyncDate: LocalDateTime,

    @Column(name = "interval", nullable = false)
    open var interval: Int

) {
    constructor() : this(
        id = 0,
        enabled = true,
        playlistName = "",
        youtubePlaylistId = "",
        spotifyPlaylistId = "",
        movesongEmail = "",
        creationDate = LocalDateTime.now(),
        lastSyncDate = LocalDateTime.now(),
        interval = 0
    )

    override fun toString(): String {
        return "Sync(id=$id, enabled=$enabled, playlistName=$playlistName, youtubePlaylistId=$youtubePlaylistId, spotifyPlaylistId=$spotifyPlaylistId, movesongEmail=$movesongEmail, creationDate=$creationDate, lastSyncDate=$lastSyncDate, interval=$interval)"
    }

    fun toDto(): SyncDto {
        return SyncDto(
            id = id,
            enabled = enabled,
            playlistName = playlistName,
            youtubePlaylistId = youtubePlaylistId,
            spotifyPlaylistId = spotifyPlaylistId,
            movesongEmail = movesongEmail,
            date = creationDate.toString(),
            lastSyncDate = lastSyncDate.toString(),
            interval = interval
        )
    }

    fun update(dto: SyncDto){
        this.enabled = dto.enabled
        this.playlistName = dto.playlistName
        this.youtubePlaylistId = dto.youtubePlaylistId
        this.spotifyPlaylistId = dto.spotifyPlaylistId
        this.movesongEmail = dto.movesongEmail
        this.creationDate = LocalDateTime.parse(dto.date)
        this.lastSyncDate = LocalDateTime.parse(dto.lastSyncDate)
        this.interval = dto.interval
    }
}