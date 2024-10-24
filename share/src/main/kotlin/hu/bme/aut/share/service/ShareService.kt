package hu.bme.aut.share.service

import hu.bme.aut.share.domain.SharePlatformType
import hu.bme.aut.share.domain.Share
import hu.bme.aut.share.repository.ShareRepository
import hu.bme.aut.shareapi.dto.req.CreateShareReq
import hu.bme.aut.shareapi.dto.req.GetShareByIdReq
import hu.bme.aut.shareapi.dto.resp.GetSharesByMovesongEmailResp
import hu.bme.aut.shareapi.dto.req.GetSharesByMovesongEmailReq
import hu.bme.aut.shareapi.dto.resp.GetShareByIdResp
import hu.bme.aut.shareapi.dto.req.UpdateShareReq
import hu.bme.aut.shareapi.dto.resp.CreateShareResp
import hu.bme.aut.shareapi.dto.resp.UpdateShareResp
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
open class ShareService(
    private val shareRepository: ShareRepository
) {

    @Transactional
    open fun createShare(req: CreateShareReq): CreateShareResp {
        return CreateShareResp(
            share = shareRepository.save(Share(
                playlistId = req.share.playlistId,
                sharedPlaylistName = req.share.sharedPlaylistName,
                ownerMovesongEmail = req.share.ownerMovesongEmail,
                visible = req.share.visible,
                views = req.share.views,
                sharePlatformType = SharePlatformType.valueOf(req.share.sharePlatformType),
                selectedBackgroundId = req.share.selectedBackgroundId,
                thumbnailUrl = req.share.thumbnailUrl
            )).toDto()
        )
    }

    @Transactional
    open fun getSharesByMovesongEmail(req: GetSharesByMovesongEmailReq): GetSharesByMovesongEmailResp {
        return GetSharesByMovesongEmailResp(
            shareRepository.findAllByOwnerMovesongEmail(req.movesongEmail).map { it.toDto() }
        )
    }

    @Transactional
    open fun getShareById(req: GetShareByIdReq): GetShareByIdResp {
        val share = shareRepository.findById(req.id).get()
        share.views++
        return GetShareByIdResp(
            share = shareRepository.save(share).toDto()
        )
    }

    @Transactional
    open fun updateShare(req: UpdateShareReq): UpdateShareResp {
        val share: Share = shareRepository.findById(req.share.id).get()
        share.update(req.share)
        return UpdateShareResp(
            share = shareRepository.save(share).toDto()
        )
    }
}
