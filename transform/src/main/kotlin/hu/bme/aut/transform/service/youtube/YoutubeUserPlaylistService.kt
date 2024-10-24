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
    open fun getPlaylist(playlistID: String, connection: Connection): Playlist {
        val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

        this.youtube = YouTube.Builder(
            YoutubeAuthorizationService.httpTransport,
            YoutubeAuthorizationService.jsonFactory,
            credential
        ).setApplicationName("Movesong").build()

        val request = youtube.playlists().list("snippet,contentDetails")
        val response = request.setMaxResults(500L)
            .setId(playlistID)
            .execute()

        return response.items[0]
    }

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
    open fun getAllPlaylists(connection: Connection): List<Playlist> {
        val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

        this.youtube = YouTube.Builder(
            YoutubeAuthorizationService.httpTransport,
            YoutubeAuthorizationService.jsonFactory,
            credential
        ).setApplicationName("Movesong").build()

        val request = youtube.playlists().list("snippet,contentDetails")
        val playlists = ArrayList<Playlist>()

        val response = request.setMaxResults(500L)
            .setMine(true)
            .execute()
        playlists.addAll(response.items)
        var lastPage = false

        do  {
            if (!response.containsKey("nextPageToken")) {
                lastPage = true
            }
            val nextPageToken = response.nextPageToken
            if(nextPageToken != null) {
                val nextPage = getPlaylistsOffset(nextPageToken, connection)
                playlists.addAll(nextPage)
            }
        } while (!lastPage)

        return playlists
    }

    @Transactional
    @Throws(IOException::class)
    open fun getPlaylistsOffset(nextPageToken: String, connection: Connection): List<Playlist> {
        val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

        this.youtube = YouTube.Builder(
            YoutubeAuthorizationService.httpTransport,
            YoutubeAuthorizationService.jsonFactory,
            credential
        ).setApplicationName("Movesong").build()

        val request = youtube.playlists().list("snippet,contentDetails").setPageToken(nextPageToken)
        val response = request.setMaxResults(500L)
            .setMine(true)
            .setPageToken(nextPageToken)
            .execute()

        return response.items
    }

    @Transactional
    @Throws(IOException::class)
    open fun getAllItemsInPlaylist(playlistID: String, connection: Connection): List<PlaylistItem> {
        val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

        this.youtube = YouTube.Builder(
            YoutubeAuthorizationService.httpTransport,
            YoutubeAuthorizationService.jsonFactory,
            credential
        ).setApplicationName("Movesong").build()

        val request = youtube.playlistItems().list("snippet,contentDetails")
        val response = request.setMaxResults(500L)
            .setPlaylistId(playlistID)
            .execute()

        val items = ArrayList<PlaylistItem>()
        items.addAll(response.items)
        var lastPage = false

        do {
            if (!response.containsKey("nextPageToken")) {
                lastPage = true
            }
            val nextPageToken = response.nextPageToken
            if(nextPageToken != null) {
                val nextPage = getItemsInPlaylistOffset(playlistID, nextPageToken, connection)
                items.addAll(nextPage.items)
            }
        } while(!lastPage)

        return items
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
        val response = request.setMaxResults(500L)
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
        val response = request.setMaxResults(500L)
            .setPlaylistId(playlistID).setPageToken(nextPageToken)
            .execute()
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