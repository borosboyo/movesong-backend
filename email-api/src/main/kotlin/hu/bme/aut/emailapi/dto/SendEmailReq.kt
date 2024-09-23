package hu.bme.aut.emailapi.dto

data class SendEmailReq(
    val userEmail: String,
    val emailType: EmailType,
    val token: String,
)