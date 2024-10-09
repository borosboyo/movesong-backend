package hu.bme.aut.transform.service.spotify

import hu.bme.aut.transform.domain.ConnectionType
import hu.bme.aut.transform.repository.ConnectionRepository
import hu.bme.aut.transform.service.youtube.YoutubeCreatePlaylistService
import hu.bme.aut.transform.service.youtube.YoutubeInsertItemInPlaylistService
import hu.bme.aut.transform.service.youtube.YoutubeSearchService
import hu.bme.aut.transformapi.dto.req.ConvertToYoutubeReq
import hu.bme.aut.transformapi.dto.resp.ConvertToYoutubeResp
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.io.IOException

@Service
open class SpotifyToYoutubeService(
    private var spotifyPlaylistTracksService: SpotifyPlaylistTracksService,
    private var spotifyGetPlaylistService: SpotifyGetPlaylistService,
    private var youtubeSearchService: YoutubeSearchService,
    private var youtubeCreatePlaylistService: YoutubeCreatePlaylistService,
    private var youtubeInsertItemInPlaylistService: YoutubeInsertItemInPlaylistService,
    private var connectionRepository: ConnectionRepository,
    private val spotifyUrlService: SpotifyUrlService
) {

    @Transactional
    @Throws(IOException::class)
    open fun convertToYoutube(req: ConvertToYoutubeReq): ConvertToYoutubeResp {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        val connection = connectionRepository.findByMovesongEmailAndConnectionType(
            req.movesongEmail,
            ConnectionType.YOUTUBE
        )
        val playlist = spotifyGetPlaylistService.getPlaylist(req.playlistId, spotifyApi)

        if (playlist != null) {
            val youtubePlaylistId: String? = youtubeCreatePlaylistService.createPlaylist(
                playlist.name, "Created by Movesong",  connection
            )
            for (t in spotifyPlaylistTracksService.getPlaylistTracks(req.playlistId, spotifyApi)) {
                val videos = youtubeSearchService.getVideos(t?.track?.name)
                if(videos != null && youtubePlaylistId != null) {
                    val videoId: String = videos[0].id.videoId
                    youtubeInsertItemInPlaylistService.insertItemInPlaylist(youtubePlaylistId, videoId, connection)
                }
            }
        }
        return ConvertToYoutubeResp(true)
    }
}