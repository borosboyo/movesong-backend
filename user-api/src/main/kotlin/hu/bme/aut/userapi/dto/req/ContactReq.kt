package hu.bme.aut.userapi.dto.req

data class ContactReq(
    val subject: String,
    val name: String,
    val email: String,
    val message: String
)