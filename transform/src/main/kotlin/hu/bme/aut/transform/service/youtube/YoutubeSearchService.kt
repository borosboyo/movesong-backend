package hu.bme.aut.transform.service.youtube

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service;
import java.io.IOException


@Service
open class YoutubeSearchService {

    private lateinit var youtube: YouTube
    private val numberOfVideosReturned: Long = 25

    @Value("\${movesong.youtube.apiKey}")
    private lateinit var youtubeApiKey: String

    @Transactional
    open fun getVideos(queryTerm: String?): List<SearchResult>? {

        try {
            youtube = YouTube.Builder(
                YoutubeAuthorizationService.httpTransport, YoutubeAuthorizationService.jsonFactory
            ) { }.setApplicationName("Movesong").build()

            val search = youtube.search().list("id,snippet")
            val apiKey: String = youtubeApiKey
            search.setKey(apiKey)
            search.setQ(queryTerm)
            search.setType("video")
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
            search.setMaxResults(numberOfVideosReturned)
            val searchResponse = search.execute()
            val searchResultList: List<SearchResult> = searchResponse.items
            return searchResultList
        } catch (e: GoogleJsonResponseException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return null
    }
}