package hu.bme.aut.email.domain

open class Email(
    var mailFrom: String,
    var mailTo: String,
    var mailSubject: String,
    var mailContent: String
)