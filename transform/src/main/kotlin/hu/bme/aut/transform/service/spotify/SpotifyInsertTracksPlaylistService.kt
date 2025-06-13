package hu.bme.aut.transform.service.spotify

import com.google.gson.Gson
import jakarta.transaction.Transactional
import org.apache.hc.core5.http.ParseException
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.special.SnapshotResult
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest
import java.io.IOException
import kotlin.collections.ArrayList
import com.google.gson.*
import se.michaelthelin.spotify.SpotifyApi


@Service
open class SpotifyInsertTracksPlaylistService {
    @Transactional
    open fun insertItemsInPlaylist(playlistId: String, urisToBeAdded: Array<String>, spotifyApi: SpotifyApi) {
        var urisList: List<String> = ArrayList(urisToBeAdded.asList())
        var under100 = false
        if (urisToBeAdded.any { it.isBlank() || !it.startsWith("spotify:track:") }) {
            throw IllegalArgumentException("Invalid Spotify URIs detected: ${urisToBeAdded.contentToString()}")
        }
        do {
            var uriArrayList: ArrayList<String>?
            if (urisList.isNotEmpty()) {
                if (urisList.size < 100) {
                    under100 = true
                    uriArrayList = ArrayList(urisList)
                } else {
                    uriArrayList = ArrayList(urisList.subList(0, 100))
                    urisList = urisList.subList(99, urisList.size - 1)
                }
                val gson = Gson()
                val data: String = gson.toJson(uriArrayList)
                val urisJson: JsonArray = JsonParser.parseString(data).asJsonArray
                val addItemsToPlaylistRequest: AddItemsToPlaylistRequest = spotifyApi
                    .addItemsToPlaylist(playlistId, urisJson).build()
                try {
                    addItemsToPlaylistRequest.execute()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                } catch (e: SpotifyWebApiException) {
                    throw RuntimeException(e)
                }
            }
        } while (!under100)
    }
}