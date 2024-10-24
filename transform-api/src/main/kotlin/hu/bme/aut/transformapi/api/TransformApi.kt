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
@Api(tags = ["transform"], value = "Transform controller to handle all transform related requests")
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

    @PostMapping(value = ["/getTransformsByOriginPlaylistId"])
    @ApiOperation(value = "Get all transformations by the origin playlist id", response = GetTransformsByOriginPlaylistIdResp::class, nickname = "getTransformsByOriginPlaylistId")
    fun getTransformsByOriginPlaylistId(@RequestBody req: GetTransformsByOriginPlaylistIdReq): ResponseEntity<GetTransformsByOriginPlaylistIdResp>

    @PostMapping(value = ["/getTransformsByDestinationPlaylistId"])
    @ApiOperation(value = "Get all transformations by the destination playlist id", response = GetTransformsByDestinationPlaylistIdResp::class, nickname = "getTransformsByDestinationPlaylistId")
    fun getTransformsByDestinationPlaylistId(@RequestBody req: GetTransformsByDestinationPlaylistIdReq): ResponseEntity<GetTransformsByDestinationPlaylistIdResp>

    @PostMapping(value = ["/getTransformsByOriginPlaylistIdAndDestinationPlaylistId"])
    @ApiOperation(value = "Get all transformations by the origin and destination playlist id", response = GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdResp::class, nickname = "getTransformsByOriginPlaylistIdAndDestinationPlaylistId")
    fun getTransformsByOriginPlaylistIdAndDestinationPlaylistId(@RequestBody req: GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdReq): ResponseEntity<GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdResp>

    @PostMapping(value = ["/getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail"])
    @ApiOperation(value = "Get all transformations by the origin and destination playlist id and movesong email", response = GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailReq::class, nickname = "getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail")
    fun getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(@RequestBody req: GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailReq): ResponseEntity<GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailResp>

    @PostMapping(value = ["/getTransformsByMovesongEmail"])
    @ApiOperation(value = "Get all transformations by the movesong email", response = GetTransformsByMovesongEmailResp::class, nickname = "getTransformsByMovesongEmail")
    fun getTransformsByMovesongEmail(@RequestBody req: GetTransformsByMovesongEmailReq): ResponseEntity<GetTransformsByMovesongEmailResp>

    //use the methods inside transformService
    @PostMapping(value = ["/getUserYoutubePlaylists"])
    @ApiOperation(value = "Get all playlists of the user's youtube account", response = GetUserYoutubePlaylistsResp::class, nickname = "getUserYoutubePlaylists")
    fun getUserYoutubePlaylists(@RequestBody req: GetUserYoutubePlaylistsReq): ResponseEntity<GetUserYoutubePlaylistsResp>

    @PostMapping(value = ["/getUserYoutubePlaylistByPlaylistId"])
    @ApiOperation(value = "Get a playlist of the user's youtube account by playlist id", response = GetUserYoutubePlaylistByPlaylistIdResp::class, nickname = "getUserYoutubePlaylistByPlaylistId")
    fun getUserYoutubePlaylistByPlaylistId(@RequestBody req: GetUserYoutubePlaylistByPlaylistIdReq): ResponseEntity<GetUserYoutubePlaylistByPlaylistIdResp>

    @PostMapping(value = ["/getUserSpotifyPlaylists"])
    @ApiOperation(value = "Get all playlists of the user's spotify account", response = GetUserSpotifyPlaylistsResp::class, nickname = "getUserSpotifyPlaylists")
    fun getUserSpotifyPlaylists(@RequestBody req: GetUserSpotifyPlaylistsReq): ResponseEntity<GetUserSpotifyPlaylistsResp>

    @PostMapping(value = ["/getUserSpotifyPlaylistByPlaylistId"])
    @ApiOperation(value = "Get a playlist of the user's spotify account by playlist id", response = GetUserSpotifyPlaylistByPlaylistIdResp::class, nickname = "getUserSpotifyPlaylistByPlaylistId")
    fun getUserSpotifyPlaylistByPlaylistId(@RequestBody req: GetUserSpotifyPlaylistByPlaylistIdReq): ResponseEntity<GetUserSpotifyPlaylistByPlaylistIdResp>

    @PostMapping(value = ["/getItemsInYoutubePlaylist"])
    @ApiOperation(value = "Get all items in a youtube playlist", response = GetItemsInYoutubePlaylistResp::class, nickname = "getItemsInYoutubePlaylist")
    fun getItemsInYoutubePlaylist(@RequestBody req: GetItemsInYoutubePlaylistReq): ResponseEntity<GetItemsInYoutubePlaylistResp>

    @PostMapping(value = ["/getItemsInSpotifyPlaylist"])
    @ApiOperation(value = "Get all items in a spotify playlist", response = GetItemsInSpotifyPlaylistResp::class, nickname = "getItemsInSpotifyPlaylist")
    fun getItemsInSpotifyPlaylist(@RequestBody req: GetItemsInSpotifyPlaylistReq): ResponseEntity<GetItemsInSpotifyPlaylistResp>

    @PostMapping(value = ["/exportYoutubePlaylistToCSV"])
    @ApiOperation(value = "Export a youtube playlist to a CSV file", response = ByteArray::class, nickname = "exportYoutubePlaylistToCSV")
    fun exportYoutubePlaylistToCSV(@RequestBody req: ExportYoutubePlaylistToFileReq): ResponseEntity<ByteArray>

    @PostMapping(value = ["/exportYoutubePlaylistToTXT"])
    @ApiOperation(value = "Export a youtube playlist to a TXT file", response = ByteArray::class, nickname = "exportYoutubePlaylistToTXT")
    fun exportYoutubePlaylistToTXT(@RequestBody req: ExportYoutubePlaylistToFileReq): ResponseEntity<ByteArray>

    @PostMapping(value = ["/exportSpotifyPlaylistToCSV"])
    @ApiOperation(value = "Export a spotify playlist to a CSV file", response = ByteArray::class, nickname = "exportSpotifyPlaylistToCSV")
    fun exportSpotifyPlaylistToCSV(@RequestBody req: ExportSpotifyPlaylistToFileReq): ResponseEntity<ByteArray>

    @PostMapping(value = ["/exportSpotifyPlaylistToTXT"])
    @ApiOperation(value = "Export a spotify playlist to a TXT file", response = ByteArray::class, nickname = "exportSpotifyPlaylistToTXT")
    fun exportSpotifyPlaylistToTXT(@RequestBody req: ExportSpotifyPlaylistToFileReq): ResponseEntity<ByteArray>
}