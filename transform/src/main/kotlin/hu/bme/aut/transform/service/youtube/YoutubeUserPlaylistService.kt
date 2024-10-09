package hu.bme.aut.transform.service.youtube

import com.google.api.client.auth.oauth2.Credential
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.*
import hu.bme.aut.transform.domain.Connection
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.io.IOException


@Service
open class YoutubeUserPlaylistService(
    private var youtubeAuthorizationService: YoutubeAuthorizationService
) {
    private lateinit var youtube: YouTube

    @Transactional
    @Throws(IOException::class)
    open fun getPlaylists(connection: Connection): List<Playlist> {
        val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

        this.youtube = YouTube.Builder(
            YoutubeAuthorizationService.httpTransport,
            YoutubeAuthorizationService.jsonFactory,
            credential
        ).setApplicationName("Movesong").build()

        val request = youtube.playlists().list("snippet,contentDetails")
        val response = request.setMaxResults(500L)
            .setMine(true)
            .execute()

        return response.items
    }

    @Transactional
    @Throws(IOException::class)
    open fun getItemsInPlaylist(playlistID: String, connection: Connection): PlaylistItemListResponse {
        val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

        this.youtube = YouTube.Builder(
            YoutubeAuthorizationService.httpTransport,
            YoutubeAuthorizationService.jsonFactory,
            credential
        ).setApplicationName("Movesong").build()

        val request = youtube.playlistItems().list("snippet,contentDetails")
        val response = request.setMaxResults(100L)
            .setPlaylistId(playlistID)
            .execute()
        return response
    }

    @Transactional
    @Throws(IOException::class)
    open fun getItemsInPlaylistOffset(playlistID: String, nextPageToken: String, connection: Connection): PlaylistItemListResponse {
        val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

        this.youtube = YouTube.Builder(
            YoutubeAuthorizationService.httpTransport,
            YoutubeAuthorizationService.jsonFactory,
            credential
        ).setApplicationName("Movesong").build()

        val request = youtube.playlistItems().list("snippet,contentDetails").setPageToken(nextPageToken)
        val response = request.setMaxResults(100L)
            .setPlaylistId(playlistID).setPageToken(nextPageToken)
            .execute()
        println(response?.items?.size)
        return response
    }

    fun getYoutubeTrackName(item: PlaylistItem): String {
        val snippet = item["snippet"] as PlaylistItemSnippet?
        return snippet!!.title
    }

    fun getYoutubeVideoId(item: PlaylistItem): String {
        val contentDetails = item.contentDetails
        return contentDetails.videoId
    }
}