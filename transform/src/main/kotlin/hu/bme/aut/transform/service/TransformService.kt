package hu.bme.aut.transform.service

import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistItem
import hu.bme.aut.transform.domain.PlatformType
import hu.bme.aut.transform.domain.Transform
import hu.bme.aut.transform.repository.ConnectionRepository
import hu.bme.aut.transform.repository.TransformRepository
import hu.bme.aut.transform.service.spotify.SpotifyGetPlaylistService
import hu.bme.aut.transform.service.spotify.SpotifyPlaylistTracksService
import hu.bme.aut.transform.service.spotify.SpotifyUrlService
import hu.bme.aut.transform.service.spotify.SpotifyUserPlaylistService
import hu.bme.aut.transform.service.youtube.YoutubeUserPlaylistService
import hu.bme.aut.transformapi.dto.PlaylistDto
import hu.bme.aut.transformapi.dto.PlaylistItemDto
import hu.bme.aut.transformapi.dto.req.*
import hu.bme.aut.transformapi.dto.resp.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Track
import java.nio.charset.StandardCharsets
import java.time.LocalDate

@Service
open class TransformService(
    private val transformRepository: TransformRepository,
    private val connectionRepository: ConnectionRepository,
    private val youtubeUserPlaylistService: YoutubeUserPlaylistService,
    private val spotifyUserPlaylistService: SpotifyUserPlaylistService,
    private val spotifyUrlService: SpotifyUrlService,
    private val spotifyPlaylistTracksService: SpotifyPlaylistTracksService,
    private val spotifyGetPlaylistService: SpotifyGetPlaylistService
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
            playlists = youtubeUserPlaylistService.getAllPlaylists(connection).map { youtubePlaylistToPlaylistDto(it) }
        )
    }

    @Transactional
    open fun getUserYoutubePlaylistByPlaylistId(req: GetUserYoutubePlaylistByPlaylistIdReq): GetUserYoutubePlaylistByPlaylistIdResp {
        val connection =
            connectionRepository.findByMovesongEmailAndPlatformType(req.movesongEmail, PlatformType.YOUTUBE)
        val playlist = youtubeUserPlaylistService.getAllPlaylists(connection).find { it.id == req.playlistId }
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
        if (spotifyPlaylist.images.isEmpty()) {
            return PlaylistDto(
                id = spotifyPlaylist.id,
                title = spotifyPlaylist.name,
                channelTitle = spotifyPlaylist.owner.displayName,
                thumbnailUrl = "",
                itemCount = spotifyPlaylist.tracks.total.toLong(),
            )
        }
        return PlaylistDto(
            id = spotifyPlaylist.id,
            title = spotifyPlaylist.name,
            channelTitle = spotifyPlaylist.owner.displayName,
            thumbnailUrl = spotifyPlaylist.images[0].url,
            itemCount = spotifyPlaylist.tracks.total.toLong(),
        )
    }

    private fun spotifyPlaylistItemToPlaylistItemDto(spotifyPlaylistItem: PlaylistTrack): PlaylistItemDto {
        val track = spotifyPlaylistItem.track as Track
        return PlaylistItemDto(
            title = track.name,
            channelTitle = track.artists[0].name,
            thumbnailUrl = track.album.images[0].url
        )
    }
}