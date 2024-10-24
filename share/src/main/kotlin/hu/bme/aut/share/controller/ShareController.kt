package hu.bme.aut.share.controller

import hu.bme.aut.share.service.ShareService
import hu.bme.aut.shareapi.api.ShareApi
import hu.bme.aut.shareapi.dto.req.CreateShareReq
import hu.bme.aut.shareapi.dto.req.GetShareByIdReq
import hu.bme.aut.shareapi.dto.req.GetSharesByMovesongEmailReq
import hu.bme.aut.shareapi.dto.req.UpdateShareReq
import hu.bme.aut.shareapi.dto.resp.CreateShareResp
import hu.bme.aut.shareapi.dto.resp.GetShareByIdResp
import hu.bme.aut.shareapi.dto.resp.GetSharesByMovesongEmailResp
import hu.bme.aut.shareapi.dto.resp.UpdateShareResp
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/share")
class ShareController(
    private val shareService: ShareService
) : ShareApi {
    override fun createShare(req: CreateShareReq): ResponseEntity<CreateShareResp> {
        return ResponseEntity.ok(shareService.createShare(req))
    }

    override fun getSharesByMovesongEmail(req: GetSharesByMovesongEmailReq): ResponseEntity<GetSharesByMovesongEmailResp> {
        return ResponseEntity.ok(shareService.getSharesByMovesongEmail(req))
    }

    override fun getShareById(req: GetShareByIdReq): ResponseEntity<GetShareByIdResp> {
        return ResponseEntity.ok(shareService.getShareById(req))
    }

    override fun updateShare(req: UpdateShareReq): ResponseEntity<UpdateShareResp> {
        return ResponseEntity.ok(shareService.updateShare(req))
    }
}
