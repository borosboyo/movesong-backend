package hu.bme.aut.transform.domain

import hu.bme.aut.transformapi.dto.TransformDto
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "transform"
)
open class Transform(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "origin_platform", nullable = false)
    @Enumerated(EnumType.STRING)
    open var originPlatform: PlatformType,

    @Column(name = "destination_platform", nullable = false)
    @Enumerated(EnumType.STRING)
    open var destinationPlatform: PlatformType,

    @Column(name = "playlist_name", nullable = false)
    open var playlistName: String,

    @Column(name = "origin_playlist_id", nullable = false)
    open var originPlaylistId: String,

    @Column(name = "destination_playlist_id", nullable = false)
    open var destinationPlaylistId: String,

    @Column(name = "movesong_email", nullable = false)
    open var movesongEmail: String,

    @Column(name = "item_count", nullable = false)
    open var itemCount: Int,

    @Column(name = "thumbnail_url", nullable = false)
    open var thumbnailUrl: String,

    @Column(name = "date", nullable = false)
    //wiith format yyyy-mm-dd
    open var date: LocalDate
) {
    constructor() : this(
        0,
        PlatformType.SPOTIFY,
        PlatformType.YOUTUBE,
        "",
        "",
        "",
        "",
        0,
        "",
        LocalDate.now()
    )

    fun toDto(): TransformDto {
        return TransformDto(
            id = id,
            originPlatform = originPlatform.name,
            destinationPlatform = destinationPlatform.name,
            playlistName = playlistName,
            originPlaylistId = originPlaylistId,
            destinationPlaylistId = destinationPlaylistId,
            movesongEmail = movesongEmail,
            itemCount = itemCount,
            thumbnailUrl = thumbnailUrl,
            date = date.toString()
        )
    }
}