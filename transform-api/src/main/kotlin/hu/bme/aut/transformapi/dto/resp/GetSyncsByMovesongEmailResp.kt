package hu.bme.aut.transformapi.dto.resp

import hu.bme.aut.transformapi.dto.SyncDto

data class GetSyncsByMovesongEmailResp(
    val syncs: List<SyncDto>
)
