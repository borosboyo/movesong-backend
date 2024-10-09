package hu.bme.aut.transform.controller

import hu.bme.aut.transform.service.ConnectionService
import hu.bme.aut.transform.service.spotify.SpotifyToYoutubeService
import hu.bme.aut.transform.service.youtube.YoutubeToSpotifyService
import hu.bme.aut.transformapi.api.TransformApi
import hu.bme.aut.transformapi.dto.req.*
import hu.bme.aut.transformapi.dto.resp.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transform")
class TransformController(
    private val connectionService: ConnectionService,
    private val spotifyToYoutubeService: SpotifyToYoutubeService,
    private val youtubeToSpotifyService: YoutubeToSpotifyService
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
}