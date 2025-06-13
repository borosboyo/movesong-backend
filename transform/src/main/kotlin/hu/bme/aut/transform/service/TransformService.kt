package hu.bme.aut.transform.service

import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistItem
import hu.bme.aut.transform.domain.Connection
import hu.bme.aut.transform.domain.PlatformType
import hu.bme.aut.transform.domain.Sync
import hu.bme.aut.transform.domain.Transform
import hu.bme.aut.transform.repository.ConnectionRepository
import hu.bme.aut.transform.repository.SyncRepository
import hu.bme.aut.transform.repository.TransformRepository
import hu.bme.aut.transform.service.spotify.*
import hu.bme.aut.transform.service.youtube.YoutubeInsertItemInPlaylistService
import hu.bme.aut.transform.service.youtube.YoutubeSearchService
import hu.bme.aut.transform.service.youtube.YoutubeUserPlaylistService
import hu.bme.aut.transformapi.dto.PlaylistDto
import hu.bme.aut.transformapi.dto.PlaylistItemDto
import hu.bme.aut.transformapi.dto.req.*
import hu.bme.aut.transformapi.dto.resp.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Track
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime

@Service
open class TransformService(
    private val transformRepository: TransformRepository,
    private val connectionRepository: ConnectionRepository,
    private val syncRepository: SyncRepository,
    private val youtubeUserPlaylistService: YoutubeUserPlaylistService,
    private val spotifyUserPlaylistService: SpotifyUserPlaylistService,
    private val spotifyUrlService: SpotifyUrlService,
    private val spotifyPlaylistTracksService: SpotifyPlaylistTracksService,
    private val spotifyGetPlaylistService: SpotifyGetPlaylistService,
    private val spotifySearchTrackService: SpotifySearchTrackService,
    private val spotifyInsertTracksPlaylistService: SpotifyInsertTracksPlaylistService,
    private val youtubeSearchService: YoutubeSearchService,
    private var youtubeInsertItemInPlaylistService: YoutubeInsertItemInPlaylistService,
) {

    @Transactional
    open fun getTransformsByOriginPlaylistId(req: GetTransformsByOriginPlaylistIdReq): GetTransformsByOriginPlaylistIdResp {
        return GetTransformsByOriginPlaylistIdResp(
            transforms = transformRepository.findAllByOriginPlaylistId(req.originPlaylistId).map { it.toDto() }
        )
    }

    @Transactional
    open fun getTransformsByDestinationPlaylistId(req: GetTransformsByDestinationPlaylistIdReq): GetTransformsByDestinationPlaylistIdResp {
        return GetTransformsByDestinationPlaylistIdResp(
            transforms = transformRepository.findAllByDestinationPlaylistId(req.destinationPlaylistId)
                .map { it.toDto() }
        )
    }

    @Transactional
    open fun getTransformsByOriginPlaylistIdAndDestinationPlaylistId(req: GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdReq): GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdResp {
        return GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdResp(
            transforms = transformRepository.findAllByOriginPlaylistIdAndDestinationPlaylistId(
                req.originPlaylistId,
                req.destinationPlaylistId
            ).map { it.toDto() }
        )
    }

    @Transactional
    open fun getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(req: GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailReq): GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailResp {
        return GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailResp(
            transforms = transformRepository.findAllByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(
                req.originPlaylistId,
                req.destinationPlaylistId,
                req.movesongEmail
            ).map { it.toDto() }
        )
    }

    @Transactional
    open fun getTransformsByMovesongEmail(req: GetTransformsByMovesongEmailReq): GetTransformsByMovesongEmailResp {
        return GetTransformsByMovesongEmailResp(
            transforms = transformRepository.findAllByMovesongEmail(req.movesongEmail).map { it.toDto() }
        )
    }

    @Transactional
    open fun getUserYoutubePlaylists(req: GetUserYoutubePlaylistsReq): GetUserYoutubePlaylistsResp {
        val connection =
            connectionRepository.findByMovesongEmailAndPlatformType(req.movesongEmail, PlatformType.YOUTUBE)
        return GetUserYoutubePlaylistsResp(
            playlists = youtubeUserPlaylistService.getPlaylists(connection).map { youtubePlaylistToPlaylistDto(it) }
        )
    }

    @Transactional
    open fun getUserYoutubePlaylistByPlaylistId(req: GetUserYoutubePlaylistByPlaylistIdReq): GetUserYoutubePlaylistByPlaylistIdResp {
        val connection =
            connectionRepository.findByMovesongEmailAndPlatformType(req.movesongEmail, PlatformType.YOUTUBE)
        val playlist = youtubeUserPlaylistService.getPlaylists(connection).find { it.id == req.playlistId }
            ?: throw IllegalArgumentException("Playlist not found")
        return GetUserYoutubePlaylistByPlaylistIdResp(
            playlist = youtubePlaylistToPlaylistDto(playlist)
        )
    }

    @Transactional
    open fun getUserSpotifyPlaylists(req: GetUserSpotifyPlaylistsReq): GetUserSpotifyPlaylistsResp {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        return GetUserSpotifyPlaylistsResp(
            playlists = spotifyUserPlaylistService.getUserPlaylists(spotifyApi).map { spotifyPlaylistToPlaylistDto(it) }
        )
    }

    @Transactional
    open fun getUserSpotifyPlaylistByPlaylistId(req: GetUserSpotifyPlaylistByPlaylistIdReq): GetUserSpotifyPlaylistByPlaylistIdResp {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        val playlist = spotifyUserPlaylistService.getUserPlaylists(spotifyApi).find { it.id == req.playlistId }
            ?: throw IllegalArgumentException("Playlist not found")
        return GetUserSpotifyPlaylistByPlaylistIdResp(
            playlist = spotifyPlaylistToPlaylistDto(playlist)
        )
    }

    @Transactional
    open fun getItemsInYoutubePlaylist(req: GetItemsInYoutubePlaylistReq): GetItemsInYoutubePlaylistResp {
        val connection =
            connectionRepository.findByMovesongEmailAndPlatformType(req.movesongEmail, PlatformType.YOUTUBE)
        return GetItemsInYoutubePlaylistResp(
            items = youtubeUserPlaylistService.getAllItemsInPlaylist(req.playlistId, connection)
                .map { youtubePlaylistItemToPlaylistItemDto(it) }
        )
    }

    @Transactional
    open fun getItemsInSpotifyPlaylist(req: GetItemsInSpotifyPlaylistReq): GetItemsInSpotifyPlaylistResp {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        return GetItemsInSpotifyPlaylistResp(
            items = spotifyPlaylistTracksService.getPlaylistTracks(req.playlistId, spotifyApi)
                .map { spotifyPlaylistItemToPlaylistItemDto(it) }
        )
    }

    @Transactional
    open fun exportYoutubePlaylistToCSV(req: ExportYoutubePlaylistToFileReq): ByteArray {
        val connection =
            connectionRepository.findByMovesongEmailAndPlatformType(req.movesongEmail, PlatformType.YOUTUBE)
        val items = youtubeUserPlaylistService.getAllItemsInPlaylist(req.playlistId, connection)
            .map { youtubePlaylistItemToPlaylistItemDto(it) }
        val playlist = youtubeUserPlaylistService.getPlaylist(req.playlistId, connection)
        transformRepository.save(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.CSV,
                playlistName = playlist.snippet.title,
                originPlaylistId = req.playlistId,
                destinationPlaylistId = "",
                movesongEmail = req.movesongEmail,
                itemCount = items.size,
                thumbnailUrl = playlist.snippet.thumbnails.default.url,
                date = LocalDate.now()
            )
        )
        return itemsToCSV(items)
    }

    @Transactional
    open fun exportYoutubePlaylistToTXT(req: ExportYoutubePlaylistToFileReq): ByteArray {
        val connection =
            connectionRepository.findByMovesongEmailAndPlatformType(req.movesongEmail, PlatformType.YOUTUBE)
        val items = youtubeUserPlaylistService.getAllItemsInPlaylist(req.playlistId, connection)
            .map { youtubePlaylistItemToPlaylistItemDto(it) }
        val playlist = youtubeUserPlaylistService.getPlaylist(req.playlistId, connection)
        transformRepository.save(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.TXT,
                playlistName = playlist.snippet.title,
                originPlaylistId = req.playlistId,
                destinationPlaylistId = "",
                movesongEmail = req.movesongEmail,
                itemCount = items.size,
                thumbnailUrl = playlist.snippet.thumbnails.default.url,
                date = LocalDate.now()
            )
        )
        return itemsToTXT(items)
    }

    @Transactional
    open fun exportSpotifyPlaylistToCSV(req: ExportSpotifyPlaylistToFileReq): ByteArray {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        val items = spotifyPlaylistTracksService.getPlaylistTracks(req.playlistId, spotifyApi)
            .map { spotifyPlaylistItemToPlaylistItemDto(it) }
        val playlist = spotifyGetPlaylistService.getPlaylist(req.playlistId, spotifyApi)
        var playlistThumbnail = ""
        if (playlist.images.isNotEmpty()) {
            playlistThumbnail = playlist.images[0].url
        }
        transformRepository.save(
            Transform(
                originPlatform = PlatformType.SPOTIFY,
                destinationPlatform = PlatformType.CSV,
                playlistName = playlist.name,
                originPlaylistId = req.playlistId,
                destinationPlaylistId = "",
                movesongEmail = req.movesongEmail,
                itemCount = items.size,
                thumbnailUrl = playlistThumbnail,
                date = LocalDate.now()
            )
        )
        return itemsToCSV(items)
    }

    @Transactional
    open fun exportSpotifyPlaylistToTXT(req: ExportSpotifyPlaylistToFileReq): ByteArray {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        val items = spotifyPlaylistTracksService.getPlaylistTracks(req.playlistId, spotifyApi)
            .map { spotifyPlaylistItemToPlaylistItemDto(it) }
        val playlist = spotifyGetPlaylistService.getPlaylist(req.playlistId, spotifyApi)
        var playlistThumbnail = ""
        if (playlist.images.isNotEmpty()) {
            playlistThumbnail = playlist.images[0].url
        }
        transformRepository.save(
            Transform(
                originPlatform = PlatformType.SPOTIFY,
                destinationPlatform = PlatformType.TXT,
                playlistName = playlist.name,
                originPlaylistId = req.playlistId,
                destinationPlaylistId = "",
                movesongEmail = req.movesongEmail,
                itemCount = items.size,
                thumbnailUrl = playlistThumbnail,
                date = LocalDate.now()
            )
        )
        return itemsToTXT(items)
    }

    @Transactional
    open fun createSync(req: CreateSyncReq): CreateSyncResp {
        val sync = Sync(
            enabled = req.sync.enabled,
            playlistName = req.sync.playlistName,
            youtubePlaylistId = req.sync.youtubePlaylistId,
            spotifyPlaylistId = req.sync.spotifyPlaylistId,
            movesongEmail = req.sync.movesongEmail,
            creationDate = LocalDateTime.now(),
            lastSyncDate = LocalDateTime.now(),
            interval = req.sync.interval
        )
        syncPlaylists(sync)
        return CreateSyncResp(
            sync = sync.toDto()
        )
    }

    @Transactional
    open fun syncPlaylists(sync: Sync): Sync {
        val youtubeConnection = connectionRepository.findByMovesongEmailAndPlatformType(sync.movesongEmail, PlatformType.YOUTUBE)
        val spotifyApi = spotifyUrlService.initSpotifyApi(sync.movesongEmail)

        val youtubeItems = youtubeUserPlaylistService.getAllItemsInPlaylist(sync.youtubePlaylistId, youtubeConnection)
            ?.associateBy { it.snippet.title } ?: emptyMap()
        val spotifyItems = spotifyPlaylistTracksService.getPlaylistTracks(sync.spotifyPlaylistId, spotifyApi)
            ?.associateBy { it.track.name } ?: emptyMap()

        syncYoutubeToSpotify(youtubeItems, spotifyItems, spotifyApi, sync.spotifyPlaylistId)
        syncSpotifyToYoutube(spotifyItems, youtubeItems, sync.youtubePlaylistId, youtubeConnection)

        return syncRepository.save(sync)
    }

    @Transactional
    open fun getSyncsByMovesongEmail(req: GetSyncsByMovesongEmailReq): GetSyncsByMovesongEmailResp {
        return GetSyncsByMovesongEmailResp(
            syncs = syncRepository.findAllByMovesongEmail(req.movesongEmail).map { it.toDto() }
        )
    }

    @Transactional
    open fun updateSync(req: UpdateSyncReq): UpdateSyncResp {
        val sync: Sync = syncRepository.findById(req.sync.id).get()
        sync.update(req.sync)
        return UpdateSyncResp(
            sync = syncRepository.save(sync).toDto()
        )
    }

    @Transactional
    open fun deleteSync(req: DeleteSyncReq): DeleteSyncResp {
        syncRepository.deleteById(req.id)
        return DeleteSyncResp(
            success = true
        )
    }

    @Transactional
    open fun deleteTransformsByMovesongEmail(req: DeleteTransformsByMovesongEmailReq): DeleteTransformsByMovesongEmailResp {
        transformRepository.deleteAllByMovesongEmail(req.movesongEmail)
        return DeleteTransformsByMovesongEmailResp(
            success = true
        )
    }

    @Transactional
    open fun deleteSyncsByMovesongEmail(req: DeleteSyncsByMovesongEmailReq): DeleteSyncsByMovesongEmailResp {
        syncRepository.deleteAllByMovesongEmail(req.movesongEmail)
        return DeleteSyncsByMovesongEmailResp(
            success = true
        )
    }

    private fun syncSpotifyToYoutube(
        spotifyItems: Map<String, PlaylistTrack>,
        youtubeItems: Map<String, PlaylistItem>,
        youtubePlaylistId: String,
        youtubeConnection: Connection
    ) {
        for (spotifyItem in spotifyItems) {
            if (!youtubeItems.containsKey(spotifyItem.key)) {
                val videos = youtubeSearchService.getVideos(spotifyItem.value.track.name)
                if (!videos.isNullOrEmpty()) {
                    youtubeInsertItemInPlaylistService.insertItemInPlaylist(
                        youtubePlaylistId,
                        videos[0].id.videoId,
                        youtubeConnection
                    )
                }
            }
        }
    }

    private fun syncYoutubeToSpotify(
        youtubeItems: Map<String, PlaylistItem>,
        spotifyItems: Map<String, PlaylistTrack>,
        spotifyApi: SpotifyApi,
        spotifyPlaylistId: String
    ) {
        val spotifyUrisToBeAdded = ArrayList<String>()
        for (youtubeItem in youtubeItems) {
            if (!spotifyItems.containsKey(youtubeItem.key)) {
                val tracks: Array<Track> =
                    spotifySearchTrackService.searchForTrack(youtubeItem.value.snippet.title, spotifyApi)

                if (tracks.isNotEmpty() && tracks[0].uri != null && !spotifyItems.values.any { it.track.uri == tracks[0].uri }) {
                    spotifyUrisToBeAdded.add(tracks[0].uri)
                }
            }
        }

        val uniqueUris = spotifyUrisToBeAdded.distinct()

        spotifyInsertTracksPlaylistService.insertItemsInPlaylist(
            spotifyPlaylistId,
            uniqueUris.toTypedArray(),
            spotifyApi
        )
    }

    private fun itemsToCSV(items: List<PlaylistItemDto>): ByteArray {
        val csvHeader = "Title,Channel Title,Thumbnail URL\n"
        val csvBody = items.joinToString("\n") { "${it.title},${it.channelTitle},${it.thumbnailUrl}" }
        return (csvHeader + csvBody).toByteArray(StandardCharsets.UTF_8)
    }

    private fun itemsToTXT(items: List<PlaylistItemDto>): ByteArray {
        val txtBody = items.joinToString("\n") { "${it.title} ${it.channelTitle} ${it.thumbnailUrl}" }
        return txtBody.toByteArray(StandardCharsets.UTF_8)
    }

    private fun youtubePlaylistToPlaylistDto(youtubePlaylist: Playlist): PlaylistDto {
        return PlaylistDto(
            id = youtubePlaylist.id,
            title = youtubePlaylist.snippet.title,
            channelTitle = youtubePlaylist.snippet.channelTitle,
            thumbnailUrl = youtubePlaylist.snippet.thumbnails.default.url,
            itemCount = youtubePlaylist.contentDetails.itemCount
        )
    }

    private fun youtubePlaylistItemToPlaylistItemDto(youtubePlaylistItem: PlaylistItem): PlaylistItemDto {
        return PlaylistItemDto(
            title = youtubePlaylistItem.snippet.title,
            channelTitle = youtubePlaylistItem.snippet.channelTitle,
            thumbnailUrl = youtubePlaylistItem.snippet.thumbnails.default.url,
        )
    }

    private fun spotifyPlaylistToPlaylistDto(spotifyPlaylist: PlaylistSimplified): PlaylistDto {
        return PlaylistDto(
            id = spotifyPlaylist.id,
            title = spotifyPlaylist.name,
            channelTitle = spotifyPlaylist.owner.displayName,
            thumbnailUrl = spotifyPlaylist.images[0].url ?: "",
            itemCount = spotifyPlaylist.tracks.total.toLong(),
        )
    }

    private fun spotifyPlaylistItemToPlaylistItemDto(spotifyPlaylistItem: PlaylistTrack): PlaylistItemDto {
        val track = spotifyPlaylistItem.track as Track
        return PlaylistItemDto(
            title = track.name,
            channelTitle = track.artists[0].name,
            thumbnailUrl = track.album.images[0].url ?: ""
        )
    }
}