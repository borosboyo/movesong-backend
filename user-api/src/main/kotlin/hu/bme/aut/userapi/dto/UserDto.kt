package hu.bme.aut.userapi.dto

import io.swagger.annotations.ApiModel

@ApiModel(value = "User")
data class UserDto(
    var id: Long,
    var username: String,
    var email: String,
    var password: String,
    var enabled: Boolean,
)