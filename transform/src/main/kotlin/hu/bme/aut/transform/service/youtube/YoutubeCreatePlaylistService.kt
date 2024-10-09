package hu.bme.aut.transform.service.youtube

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.*
import hu.bme.aut.transform.domain.Connection
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.io.IOException

@Service
open class YoutubeCreatePlaylistService(
    private var youtubeAuthorizationService: YoutubeAuthorizationService
) {
    private lateinit var youtube: YouTube

    @Transactional
    @Throws(IOException::class)
    open fun insertPlaylist(title: String, description: String): String {

        val playlistSnippet = PlaylistSnippet()
        playlistSnippet.setTitle(title)
        playlistSnippet.setDescription(description)
        val playlistStatus = PlaylistStatus()
        playlistStatus.setPrivacyStatus("public")

        val youTubePlaylist = Playlist()
        youTubePlaylist.setSnippet(playlistSnippet)
        youTubePlaylist.setStatus(playlistStatus)

        val playlistInsertCommand =
            this.youtube.playlists().insert("snippet,status", youTubePlaylist)
        val playlistInserted: Playlist = playlistInsertCommand.execute()
        return playlistInserted.id
    }


    @Transactional
    open fun createPlaylist(title: String, description: String, connection: Connection): String? {
        return try {
            val credential: Credential = youtubeAuthorizationService.getCredentialFromConnection(connection)

            this.youtube = YouTube.Builder(
                YoutubeAuthorizationService.httpTransport,
                YoutubeAuthorizationService.jsonFactory,
                credential
            ).setApplicationName("Movesong").build()

            val playlistId = insertPlaylist(title, description)
            println(playlistId)
            playlistId

        } catch (e: GoogleJsonResponseException) {
            println("There was a service error: ${e.details.code} : ${e.details.message}")
            e.printStackTrace()
            null
        } catch (e: IOException) {
            println("IOException: ${e.message}")
            e.printStackTrace()
            null
        } catch (t: Throwable) {
            println("Throwable: ${t.message}")
            t.printStackTrace()
            null
        }
    }
}