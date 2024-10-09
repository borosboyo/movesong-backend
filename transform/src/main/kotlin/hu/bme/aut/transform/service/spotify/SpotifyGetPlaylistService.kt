package hu.bme.aut.transform.service.spotify

import jakarta.transaction.Transactional
import org.apache.hc.core5.http.ParseException
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.Playlist
import java.io.IOException

@Service
open class SpotifyGetPlaylistService {

    @Transactional
    open fun getPlaylist(playlistId: String, spotifyApi: SpotifyApi): Playlist? {
        val getPlaylistRequest = spotifyApi.getPlaylist(playlistId).build()

        try {
            val playlist: Playlist = getPlaylistRequest.execute()
            return playlist
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        } catch (e: SpotifyWebApiException) {
            throw RuntimeException(e)
        }
        return null
    }
}