package hu.bme.aut.userapi.dto.resp

data class LoginResp(
    val id: Long,
    val accessToken: String,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String
)