package hu.bme.aut.shareapi.dto.resp

import hu.bme.aut.shareapi.dto.ShareDto

data class GetSharesByMovesongEmailResp(
    val shares: List<ShareDto>
)
