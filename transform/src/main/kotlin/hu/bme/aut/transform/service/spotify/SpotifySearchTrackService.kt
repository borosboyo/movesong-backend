package hu.bme.aut.transform.service.spotify

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import java.io.IOException
import se.michaelthelin.spotify.model_objects.special.SearchResult
import se.michaelthelin.spotify.model_objects.specification.Track
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest
import org.apache.hc.core5.http.ParseException
import se.michaelthelin.spotify.SpotifyApi


@Service
open class SpotifySearchTrackService {

    @Transactional
    open fun searchForTrack(title: String, spotifyApi: SpotifyApi): Array<Track> {
        val searchItemRequest: SearchItemRequest = spotifyApi.searchItem(title, "track").build()

        try {
            val searchResult: SearchResult = searchItemRequest.execute()
            val tracks: Array<Track> = searchResult.tracks.items
            return tracks
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        } catch (e: SpotifyWebApiException) {
            throw RuntimeException(e)
        }
    }
}