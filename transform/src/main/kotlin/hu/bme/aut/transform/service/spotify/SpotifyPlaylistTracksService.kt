package hu.bme.aut.transform.service.spotify

import jakarta.transaction.Transactional
import org.apache.hc.core5.http.ParseException
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest
import java.io.IOException


@Service
open class SpotifyPlaylistTracksService {
    @Transactional
    open fun getPlaylistTracks(playlistId: String, spotifyApi: SpotifyApi): Array<PlaylistTrack> {
        val getPlaylistsItemsRequest: GetPlaylistsItemsRequest =
            spotifyApi.getPlaylistsItems(playlistId).build()
        try {
            val playlistTracks = getPlaylistsItemsRequest.execute().items
            return playlistTracks
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        } catch (e: SpotifyWebApiException) {
            throw RuntimeException(e)
        }
    }
}