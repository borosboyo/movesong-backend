package hu.bme.aut.transformapi.dto.req

data class GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailReq(
    val originPlaylistId: String,
    val destinationPlaylistId: String,
    val movesongEmail: String
)
