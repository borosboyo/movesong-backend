package hu.bme.aut.transformapi.dto.req

data class GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdReq(
    val originPlaylistId: String,
    val destinationPlaylistId: String
)
