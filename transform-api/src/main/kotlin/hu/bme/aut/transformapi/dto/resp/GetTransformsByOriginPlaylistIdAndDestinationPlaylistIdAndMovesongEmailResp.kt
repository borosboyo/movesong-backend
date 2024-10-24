package hu.bme.aut.transformapi.dto.resp

import hu.bme.aut.transformapi.dto.TransformDto

data class GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailResp(
    val transforms: List<TransformDto>
)
