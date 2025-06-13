package hu.bme.aut.transform.service.youtube

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.PlaylistItemSnippet
import com.google.api.services.youtube.model.ResourceId
import hu.bme.aut.transform.domain.Connection
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.io.IOException


@Service
open class YoutubeInsertItemInPlaylistService(
    private var youtubeAuthorizationService: YoutubeAuthorizationService
) {
    private lateinit var youtube: YouTube

    @Throws(IOException::class)
    private fun insertPlaylistItem(playlistId: String, videoId: String): String {
        val resourceId = ResourceId()
        resourceId.setKind("youtube#video")
        resourceId.setVideoId(videoId)

        val playlistItemSnippet = PlaylistItemSnippet()
        playlistItemSnippet.setTitle("First video in the test playlist")
        playlistItemSnippet.setPlaylistId(playlistId)
        playlistItemSnippet.setResourceId(resourceId)

        val playlistItem = PlaylistItem()
        playlistItem.setSnippet(playlistItemSnippet)

        val playlistItemsInsertCommand: YouTube.PlaylistItems.Insert =
            this.youtube.playlistItems().insert("snippet,contentDetails", playlistItem)
        val returnedPlaylistItem = playlistItemsInsertCommand.execute()

        return returnedPlaylistItem.id
    }

    @Throws(IOException::class)
    @Transactional
    open fun insertItemInPlaylist(playlistId: String, videoId: String, connection: Connection) {
        try {
            val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

            this.youtube = YouTube.Builder(
                YoutubeAuthorizationService.httpTransport,
                YoutubeAuthorizationService.jsonFactory,
                credential
            ).setApplicationName("Movesong").build()

            val playlistItem = insertPlaylistItem(playlistId, videoId)
        } catch (e: GoogleJsonResponseException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}