package hu.bme.aut.userapi.dto.req

data class RegisterReq(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)
