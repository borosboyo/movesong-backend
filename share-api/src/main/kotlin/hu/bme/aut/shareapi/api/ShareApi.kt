package hu.bme.aut.shareapi.api

import hu.bme.aut.shareapi.dto.req.CreateShareReq
import hu.bme.aut.shareapi.dto.req.GetShareByIdReq
import hu.bme.aut.shareapi.dto.req.GetSharesByMovesongEmailReq
import hu.bme.aut.shareapi.dto.req.UpdateShareReq
import hu.bme.aut.shareapi.dto.resp.CreateShareResp
import hu.bme.aut.shareapi.dto.resp.GetShareByIdResp
import hu.bme.aut.shareapi.dto.resp.GetSharesByMovesongEmailResp
import hu.bme.aut.shareapi.dto.resp.UpdateShareResp
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "share")
@Api(tags = ["share"], value = "Share controller to handle all share related requests")
interface ShareApi {
    @PostMapping(value = ["/createShare"])
    @ApiOperation(value = "Create a share", response = CreateShareResp::class, nickname = "createShare")
    fun createShare(@RequestBody req: CreateShareReq): ResponseEntity<CreateShareResp>

    @PostMapping(value = ["/getSharesByMovesongEmail"])
    @ApiOperation(value = "Get all shares by the movesong email", response = GetSharesByMovesongEmailResp::class, nickname = "getSharesByMovesongEmail")
    fun getSharesByMovesongEmail(@RequestBody req: GetSharesByMovesongEmailReq): ResponseEntity<GetSharesByMovesongEmailResp>

    @PostMapping(value = ["/getShareById"])
    @ApiOperation(value = "Get a share by the id", response = GetShareByIdResp::class, nickname = "getShareById")
    fun getShareById(@RequestBody req: GetShareByIdReq): ResponseEntity<GetShareByIdResp>

    @PostMapping(value = ["/updateShare"])
    @ApiOperation(value = "Update a share", response = UpdateShareResp::class, nickname = "updateShare")
    fun updateShare(@RequestBody req: UpdateShareReq): ResponseEntity<UpdateShareResp>
}