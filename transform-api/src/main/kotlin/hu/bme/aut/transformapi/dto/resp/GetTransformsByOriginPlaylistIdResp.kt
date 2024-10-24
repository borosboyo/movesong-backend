package hu.bme.aut.transformapi.dto.resp

import hu.bme.aut.transformapi.dto.TransformDto

data class GetTransformsByOriginPlaylistIdResp(
    val transforms: List<TransformDto>
)
