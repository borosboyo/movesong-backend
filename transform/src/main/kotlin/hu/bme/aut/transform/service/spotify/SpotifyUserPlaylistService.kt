package hu.bme.aut.transform.service.spotify

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import java.io.IOException
import org.apache.hc.core5.http.ParseException
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest


@Service
open class SpotifyUserPlaylistService() {

    @Transactional
    open fun getUserPlaylists(spotifyApi: SpotifyApi): Array<PlaylistSimplified?> {
        val getListOfUsersPlaylistsRequest: GetListOfCurrentUsersPlaylistsRequest =
            spotifyApi.listOfCurrentUsersPlaylists
                .build()
        try {
            return getListOfUsersPlaylistsRequest.execute().items
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        } catch (e: SpotifyWebApiException) {
            throw RuntimeException(e)
        }
    }
}