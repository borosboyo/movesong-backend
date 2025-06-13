package hu.bme.aut.share.service

import hu.bme.aut.share.domain.SharePlatformType
import hu.bme.aut.share.domain.Share
import hu.bme.aut.share.repository.ShareRepository
import hu.bme.aut.shareapi.dto.req.*
import hu.bme.aut.shareapi.dto.resp.*
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
                movesongEmail = req.share.movesongEmail,
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
            shareRepository.findAllBymovesongEmail(req.movesongEmail).map { it.toDto() }
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

    @Transactional
    open fun deleteShares(req: DeleteSharesReq): DeleteSharesResp {
        shareRepository.deleteAllByMovesongEmail(req.movesongEmail)
        return DeleteSharesResp(
            success = true
        )
    }
}
