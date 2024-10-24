package hu.bme.aut.transform.controller

import hu.bme.aut.transform.service.ConnectionService
import hu.bme.aut.transform.service.TransformService
import hu.bme.aut.transform.service.spotify.SpotifyToYoutubeService
import hu.bme.aut.transform.service.youtube.YoutubeToSpotifyService
import hu.bme.aut.transformapi.api.TransformApi
import hu.bme.aut.transformapi.dto.req.*
import hu.bme.aut.transformapi.dto.resp.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@RestController
@RequestMapping("/transform")
class TransformController(
    private val connectionService: ConnectionService,
    private val spotifyToYoutubeService: SpotifyToYoutubeService,
    private val youtubeToSpotifyService: YoutubeToSpotifyService,
    private val transformService: TransformService
): TransformApi {
    override fun connectYoutubeAccount(req: ConnectYoutubeAccountReq): ResponseEntity<ConnectYoutubeAccountResp> {
        return ResponseEntity.ok(connectionService.connectYoutubeAccount(req))
    }

    override fun connectSpotifyAccount(req: ConnectSpotifyAccountReq): ResponseEntity<ConnectSpotifyAccountResp> {
        return ResponseEntity.ok(connectionService.connectSpotifyAccount(req))
    }

    override fun findConnectionsByMovesongEmail(req: FindConnectionsByMovesongEmailReq): ResponseEntity<FindConnectionsByMovesongEmailResp> {
        return ResponseEntity.ok(connectionService.findConnectionsByMovesongEmail(req))
    }

    override fun convertToSpotify(req: ConvertToSpotifyReq): ResponseEntity<ConvertToSpotifyResp> {
        return ResponseEntity.ok(youtubeToSpotifyService.convertToSpotify(req))
    }

    override fun convertToYoutube(req: ConvertToYoutubeReq): ResponseEntity<ConvertToYoutubeResp> {
        return ResponseEntity.ok(spotifyToYoutubeService.convertToYoutube(req))
    }

    override fun getTransformsByOriginPlaylistId(req: GetTransformsByOriginPlaylistIdReq): ResponseEntity<GetTransformsByOriginPlaylistIdResp> {
        return ResponseEntity.ok(transformService.getTransformsByOriginPlaylistId(req))
    }

    override fun getTransformsByDestinationPlaylistId(req: GetTransformsByDestinationPlaylistIdReq): ResponseEntity<GetTransformsByDestinationPlaylistIdResp> {
        return ResponseEntity.ok(transformService.getTransformsByDestinationPlaylistId(req))
    }

    override fun getTransformsByOriginPlaylistIdAndDestinationPlaylistId(req: GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdReq): ResponseEntity<GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdResp> {
        return ResponseEntity.ok(transformService.getTransformsByOriginPlaylistIdAndDestinationPlaylistId(req))
    }

    override fun getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(req: GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailReq): ResponseEntity<GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailResp> {
        return ResponseEntity.ok(transformService.getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(req))
    }

    override fun getTransformsByMovesongEmail(req: GetTransformsByMovesongEmailReq): ResponseEntity<GetTransformsByMovesongEmailResp> {
        return ResponseEntity.ok(transformService.getTransformsByMovesongEmail(req))
    }

    override fun getUserYoutubePlaylists(req: GetUserYoutubePlaylistsReq): ResponseEntity<GetUserYoutubePlaylistsResp> {
        return ResponseEntity.ok(transformService.getUserYoutubePlaylists(req))
    }

    override fun getUserYoutubePlaylistByPlaylistId(req: GetUserYoutubePlaylistByPlaylistIdReq): ResponseEntity<GetUserYoutubePlaylistByPlaylistIdResp> {
        return ResponseEntity.ok(transformService.getUserYoutubePlaylistByPlaylistId(req))
    }

    override fun getUserSpotifyPlaylists(req: GetUserSpotifyPlaylistsReq): ResponseEntity<GetUserSpotifyPlaylistsResp> {
        return ResponseEntity.ok(transformService.getUserSpotifyPlaylists(req))
    }

    override fun getUserSpotifyPlaylistByPlaylistId(req: GetUserSpotifyPlaylistByPlaylistIdReq): ResponseEntity<GetUserSpotifyPlaylistByPlaylistIdResp> {
        return ResponseEntity.ok(transformService.getUserSpotifyPlaylistByPlaylistId(req))
    }

    override fun getItemsInYoutubePlaylist(req: GetItemsInYoutubePlaylistReq): ResponseEntity<GetItemsInYoutubePlaylistResp> {
        return ResponseEntity.ok(transformService.getItemsInYoutubePlaylist(req))
    }

    override fun getItemsInSpotifyPlaylist(req: GetItemsInSpotifyPlaylistReq): ResponseEntity<GetItemsInSpotifyPlaylistResp> {
        return ResponseEntity.ok(transformService.getItemsInSpotifyPlaylist(req))
    }

    override fun exportYoutubePlaylistToCSV(req: ExportYoutubePlaylistToFileReq): ResponseEntity<ByteArray> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.parseMediaType("text/csv")
            setContentDispositionFormData("attachment", "youtube_playlist.csv")
        }
        return ResponseEntity(transformService.exportYoutubePlaylistToCSV(req), headers, HttpStatus.OK)
    }

    override fun exportYoutubePlaylistToTXT(req: ExportYoutubePlaylistToFileReq): ResponseEntity<ByteArray> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.TEXT_PLAIN
            setContentDispositionFormData("attachment", "youtube_playlist.txt")
        }
        return ResponseEntity(transformService.exportYoutubePlaylistToTXT(req), headers, HttpStatus.OK)
    }

    override fun exportSpotifyPlaylistToCSV(req: ExportSpotifyPlaylistToFileReq): ResponseEntity<ByteArray> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.parseMediaType("text/csv")
            setContentDispositionFormData("attachment", "spotify_playlist.csv")
        }
        return ResponseEntity(transformService.exportSpotifyPlaylistToCSV(req), headers, HttpStatus.OK)

    }

    override fun exportSpotifyPlaylistToTXT(req: ExportSpotifyPlaylistToFileReq): ResponseEntity<ByteArray> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.TEXT_PLAIN
            setContentDispositionFormData("attachment", "spotify_playlist.txt")
        }
        return ResponseEntity(transformService.exportSpotifyPlaylistToTXT(req), headers, HttpStatus.OK) }
}