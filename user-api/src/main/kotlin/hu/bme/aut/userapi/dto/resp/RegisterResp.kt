package hu.bme.aut.userapi.dto.resp

data class RegisterResp(
    var email: String,
    var username: String,
    var success: Boolean = false,
)
