package hu.bme.aut.emailapi.dto

enum class EmailType(val emailSubject: String, val emailTemplate: String) {

    RESET_PASSWORD("Elfelejtett jelszó", "reset-password"),
    CONFIRM_REGISTRATION("Regisztráció megerősítése", "confirm-registration"),
    CONTACT("Kapcsolat", "contact");
}