package hu.bme.aut.transform.service.spotify

import hu.bme.aut.transform.domain.PlatformType
import hu.bme.aut.transform.domain.Transform
import hu.bme.aut.transform.repository.ConnectionRepository
import hu.bme.aut.transform.repository.TransformRepository
import hu.bme.aut.transform.service.youtube.YoutubeCreatePlaylistService
import hu.bme.aut.transform.service.youtube.YoutubeInsertItemInPlaylistService
import hu.bme.aut.transform.service.youtube.YoutubeSearchService
import hu.bme.aut.transformapi.dto.req.ConvertToYoutubeReq
import hu.bme.aut.transformapi.dto.resp.ConvertToYoutubeResp
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDate

@Service
open class SpotifyToYoutubeService(
    private var spotifyPlaylistTracksService: SpotifyPlaylistTracksService,
    private var spotifyGetPlaylistService: SpotifyGetPlaylistService,
    private var youtubeSearchService: YoutubeSearchService,
    private var youtubeCreatePlaylistService: YoutubeCreatePlaylistService,
    private var youtubeInsertItemInPlaylistService: YoutubeInsertItemInPlaylistService,
    private var connectionRepository: ConnectionRepository,
    private var transformRepository: TransformRepository,
    private val spotifyUrlService: SpotifyUrlService
) {

    @Transactional
    @Throws(IOException::class)
    open fun convertToYoutube(req: ConvertToYoutubeReq): ConvertToYoutubeResp {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        val connection = connectionRepository.findByMovesongEmailAndPlatformType(
            req.movesongEmail,
            PlatformType.YOUTUBE
        )
        val playlist = spotifyGetPlaylistService.getPlaylist(req.playlistId, spotifyApi)

        if (playlist != null) {
            val youtubePlaylistId: String? = youtubeCreatePlaylistService.createPlaylist(
                playlist.name, "Created by Movesong", connection
            )
            for (t in spotifyPlaylistTracksService.getPlaylistTracks(req.playlistId, spotifyApi)) {
                val videos = youtubeSearchService.getVideos(t.track?.name)
                if (videos != null && youtubePlaylistId != null) {
                    val videoId: String = videos[0].id.videoId
                    youtubeInsertItemInPlaylistService.insertItemInPlaylist(youtubePlaylistId, videoId, connection)
                } 
            }
            transformRepository.save(
                Transform(
                    originPlatform = PlatformType.SPOTIFY,
                    destinationPlatform = PlatformType.YOUTUBE,
                    playlistName = playlist.name,
                    originPlaylistId = req.playlistId,
                    destinationPlaylistId = youtubePlaylistId!!,
                    movesongEmail = req.movesongEmail,
                    itemCount = playlist.tracks.total,
                    thumbnailUrl = playlist.images[0].url,
                    date = LocalDate.now()
                )
            )
            return ConvertToYoutubeResp(false, youtubePlaylistId)
        }
        return ConvertToYoutubeResp(false, "")
    }
}