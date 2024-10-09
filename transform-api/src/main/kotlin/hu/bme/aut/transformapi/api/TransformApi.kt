package hu.bme.aut.transformapi.api

import hu.bme.aut.transformapi.dto.req.*
import hu.bme.aut.transformapi.dto.resp.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "transform")
@Api(tags = ["transform"], value = "Transform controller to handle all connection related requests")
interface TransformApi {
    @PostMapping(value = ["/connectYoutubeAccount"])
    @ApiOperation(value = "Connect the user's youtube account", response = ConnectYoutubeAccountResp::class, nickname = "connectYoutubeAccount")
    fun connectYoutubeAccount(@RequestBody req: ConnectYoutubeAccountReq): ResponseEntity<ConnectYoutubeAccountResp>

    @PostMapping(value = ["/connectSpotifyAccount"])
    @ApiOperation(value = "Connect the user's spotify account", response = ConnectSpotifyAccountResp::class, nickname = "connectSpotifyAccount")
    fun connectSpotifyAccount(@RequestBody req: ConnectSpotifyAccountReq): ResponseEntity<ConnectSpotifyAccountResp>

    @PostMapping(value = ["/findConnectionsByMovesongEmail"])
    @ApiOperation(value = "Find all connections by the user's movesong email", response = FindConnectionsByMovesongEmailResp::class, nickname = "findConnectionsByMovesongEmail")
    fun findConnectionsByMovesongEmail(@RequestBody req: FindConnectionsByMovesongEmailReq): ResponseEntity<FindConnectionsByMovesongEmailResp>

    @PostMapping(value = ["/convertToSpotify"])
    @ApiOperation(value = "Convert a youtube playlist to a spotify playlist", response = ConvertToSpotifyResp::class, nickname = "convertToSpotify")
    fun convertToSpotify(@RequestBody req: ConvertToSpotifyReq): ResponseEntity<ConvertToSpotifyResp>

    @PostMapping(value = ["/convertToYoutube"])
    @ApiOperation(value = "Convert a spotify playlist to a youtube playlist", response = ConvertToYoutubeResp::class, nickname = "convertToYoutube")
    fun convertToYoutube(@RequestBody req: ConvertToYoutubeReq): ResponseEntity<ConvertToYoutubeResp>
}