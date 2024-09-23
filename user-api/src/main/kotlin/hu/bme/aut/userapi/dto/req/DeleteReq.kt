package hu.bme.aut.userapi.dto.req

import hu.bme.aut.userapi.dto.UserDto

data class DeleteReq(
    val user: UserDto
)