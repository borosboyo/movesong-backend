package hu.bme.aut.share.domain

import hu.bme.aut.shareapi.dto.ShareDto
import jakarta.persistence.*

@Entity
@Table(
    name = "share"
)
open class Share (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "playlist_id", nullable = false)
    open var playlistId: String,

    @Column(name = "shared_playlist_name", nullable = false)
    open var sharedPlaylistName: String,

    @Column(name = "user_id", nullable = false)
    open var movesongEmail: String,

    @Column(name = "visible", nullable = false)
    open var visible: Boolean,

    @Column(name = "views", nullable = false)
    open var views: Int = 0,

    @Column(name = "origin_platform_type", nullable = false)
    open var sharePlatformType: SharePlatformType,

    @Column(name = "selectedBackgroundId", nullable = false)
    open var selectedBackgroundId: Long,

    @Column(name = "thumbnailUrl", nullable = false)
    open var thumbnailUrl: String

) {
    constructor() : this(
        0,
        "",
        "",
        "",
        true,
        0,
        SharePlatformType.YOUTUBE,
        0,
        ""
    )

    fun toDto(): ShareDto {
        return ShareDto(
            id = id,
            playlistId = playlistId,
            sharedPlaylistName = sharedPlaylistName,
            movesongEmail = movesongEmail,
            visible = visible,
            views = views,
            sharePlatformType = sharePlatformType.name,
            selectedBackgroundId = selectedBackgroundId,
            thumbnailUrl = thumbnailUrl
        )
    }

    fun update(dto: ShareDto){
        this.playlistId = dto.playlistId
        this.sharedPlaylistName = dto.sharedPlaylistName
        this.movesongEmail = dto.movesongEmail
        this.visible = dto.visible
        this.views = dto.views
        this.sharePlatformType = SharePlatformType.valueOf(dto.sharePlatformType)
        this.selectedBackgroundId = dto.selectedBackgroundId
        this.thumbnailUrl = dto.thumbnailUrl
    }
}