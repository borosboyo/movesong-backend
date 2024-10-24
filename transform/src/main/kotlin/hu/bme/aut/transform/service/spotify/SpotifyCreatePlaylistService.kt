package hu.bme.aut.transform.service.spotify

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.io.IOException
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest
import org.apache.hc.core5.http.ParseException
import se.michaelthelin.spotify.SpotifyApi


@Service
open class SpotifyCreatePlaylistService(
    private var spotifyUserProfileService: SpotifyUserProfileService
) {
    @Transactional
    open fun createPlaylist(title: String, description: String, spotifyApi: SpotifyApi): Playlist {
        val userID = spotifyUserProfileService.getCurrentUserProfile(spotifyApi)!!.id
        val createPlaylistRequest: CreatePlaylistRequest = spotifyApi.createPlaylist(userID, title).build()

        try {
            val createdPlaylist = createPlaylistRequest.execute()
            return createdPlaylist
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        } catch (e: SpotifyWebApiException) {
            throw RuntimeException(e)
        }
    }
}