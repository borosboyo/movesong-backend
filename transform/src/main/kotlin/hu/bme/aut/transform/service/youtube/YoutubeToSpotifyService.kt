package hu.bme.aut.transform.service.youtube

import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistItem
import hu.bme.aut.transform.domain.PlatformType
import hu.bme.aut.transform.domain.TransformTrackModel
import hu.bme.aut.transform.domain.Transform
import hu.bme.aut.transform.repository.ConnectionRepository
import hu.bme.aut.transform.repository.TransformRepository
import hu.bme.aut.transform.service.spotify.*
import hu.bme.aut.transformapi.dto.req.ConvertToSpotifyReq
import hu.bme.aut.transformapi.dto.resp.ConvertToSpotifyResp
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Track
import java.io.IOException
import java.time.LocalDate

@Service
open class YoutubeToSpotifyService(
    private val spotifyUrlService: SpotifyUrlService,
    private val spotifyCreatePlaylistService: SpotifyCreatePlaylistService,
    private val spotifySearchTrackService: SpotifySearchTrackService,
    private val spotifyInsertTracksPlaylistService: SpotifyInsertTracksPlaylistService,
    private val youtubeUserPlaylistService: YoutubeUserPlaylistService,
    private val connectionRepository: ConnectionRepository,
    private val transformRepository: TransformRepository
) {
    @Transactional
    @Throws(IOException::class)
    open fun convertToSpotify(req: ConvertToSpotifyReq): ConvertToSpotifyResp {
        val spotifyApi = spotifyUrlService.initSpotifyApi(req.movesongEmail)
        val connection = connectionRepository.findByMovesongEmailAndPlatformType(
            req.movesongEmail,
            PlatformType.YOUTUBE
        )
        var spotifyPlaylist: se.michaelthelin.spotify.model_objects.specification.Playlist? = null
        var youtubePlaylist: Playlist? = null
        for (p in youtubeUserPlaylistService.getPlaylists(connection)) {
            if (req.playlistId == p.id) {
                youtubePlaylist = p
                spotifyPlaylist = spotifyCreatePlaylistService.createPlaylist(
                    p.snippet.title,
                    "Created by Movesong",
                    spotifyApi
                )
                println("Generated a playlist Successfully: " + spotifyPlaylist.name)
            }
        }
        val spotifyUrisToBeAdded = ArrayList<String>()
        var playlistItemsListResponse = youtubeUserPlaylistService.getItemsInPlaylist(req.playlistId, connection)
        var lastPage = false

        do {
            if (!playlistItemsListResponse.containsKey("nextPageToken")) {
                lastPage = true
            }
            val nextPageToken = playlistItemsListResponse.nextPageToken
            val playlistItems = playlistItemsListResponse.items
            for (s in convertSpotifyTrack(playlistItems, spotifyApi)) {
                spotifyUrisToBeAdded.add(s)
            }
            if (nextPageToken != null) {
                playlistItemsListResponse = youtubeUserPlaylistService.getItemsInPlaylistOffset(req.playlistId, nextPageToken, connection)
            }
        } while (!lastPage)

        val urisToBeAdded = spotifyUrisToBeAdded.toTypedArray<String>()
        spotifyInsertTracksPlaylistService.insertItemsInPlaylist(spotifyPlaylist!!.id, urisToBeAdded, spotifyApi)
        transformRepository.save(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.SPOTIFY,
                playlistName = spotifyPlaylist.name,
                originPlaylistId = req.playlistId,
                destinationPlaylistId = spotifyPlaylist.id,
                movesongEmail = req.movesongEmail,
                itemCount = spotifyUrisToBeAdded.size,
                thumbnailUrl = youtubePlaylist!!.snippet.thumbnails.default.url,
                date = LocalDate.now()
            )
        )
        return ConvertToSpotifyResp(true, spotifyPlaylist.id)
    }

    private fun convertSpotifyTrack(playlistItemList: List<PlaylistItem?>, spotifyApi: SpotifyApi): ArrayList<String> {
        val youtubeSpotifyComparison = ArrayList<Any>()
        val spotifyUrisToBeAdded = ArrayList<String>()

        for (playlistItem in playlistItemList) {
            val track: Array<Track> = spotifySearchTrackService
                .searchForTrack(
                    youtubeUserPlaylistService.getYoutubeTrackName(playlistItem!!),
                    spotifyApi
                )

            if (track.isNotEmpty()) {
                val newTrack = TransformTrackModel(
                    youtubeUserPlaylistService.getYoutubeTrackName(playlistItem),
                    track[0].name
                )
                youtubeSpotifyComparison.add(newTrack)
                spotifyUrisToBeAdded.add(track[0].uri)
            }
        }
        return spotifyUrisToBeAdded
    }
}
