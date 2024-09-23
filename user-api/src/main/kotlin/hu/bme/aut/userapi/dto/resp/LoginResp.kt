package hu.bme.aut.userapi.dto.resp

import hu.bme.aut.userapi.dto.UserDto

data class LoginResp(
    val userDto: UserDto,
    val accessToken: String,
)