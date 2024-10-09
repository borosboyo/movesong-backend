package hu.bme.aut.transformapi.dto.resp

import hu.bme.aut.transformapi.dto.ConnectionDto

data class FindConnectionsByMovesongEmailResp(
    val connections: List<ConnectionDto>? = null
)
