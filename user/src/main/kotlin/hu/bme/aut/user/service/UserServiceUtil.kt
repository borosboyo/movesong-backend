package hu.bme.aut.user.service

import com.sanctionco.jmail.JMail
import java.nio.charset.Charset
import java.security.SecureRandom
import java.sql.Timestamp
import java.util.*
import java.util.regex.Pattern

class UserServiceUtil {
    companion object {
        fun generateRandomString(): String {
            val array = ByteArray(12)
            SecureRandom().nextBytes(array)
            return String(array, Charset.forName("UTF-8"))
        }

        fun isNameValid(name: String): Boolean {
            return isStringNotNullOrEmpty(name) && !hasSpecialCharacters(name)
        }

        fun isUsernameValid(username: String): Boolean {
            return isStringNotNullOrEmpty(username)
                    && !hasSpecialCharactersOtherThanUnderScoreAndPoint(username)
                    && username.length >= 4
        }

        fun isEmailValid(email: String): Boolean {
            return isStringNotNullOrEmpty(email) && JMail.isValid(email)
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= 8 && hasSpecialCharacters(password)
                    && hasUppercaseCharacters(password)
                    && hasNumbers(password)
        }

        fun calculateVerificationTokenExpiryDate(expiryTimeInMinutes: Int): Date {
            val cal = Calendar.getInstance()
            cal.time = Timestamp(cal.time.time)
            cal.add(Calendar.MINUTE, expiryTimeInMinutes)
            return Date(cal.time.time)
        }

        private fun hasSpecialCharactersOtherThanUnderScoreAndPoint(str: String): Boolean {
            val special = Pattern.compile("[^A-Za-z0-9_.]", Pattern.CASE_INSENSITIVE)
            return special.matcher(str).find()
        }

        private fun hasSpecialCharacters(str: String): Boolean {
            val special = Pattern.compile("[^A-ZŐÚÉÁŰÖÜÓa-zéáűőúöüó0-9]", Pattern.CASE_INSENSITIVE)
            return special.matcher(str).find()
        }

        private fun hasUppercaseCharacters(str: String): Boolean {
            val special = Pattern.compile("[A-Z]", Pattern.CASE_INSENSITIVE)
            return special.matcher(str).find()
        }

        private fun hasNumbers(str: String): Boolean {
            val special = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE)
            return special.matcher(str).find()
        }

        private fun isStringNotNullOrEmpty(str: String): Boolean {
            return str != ""
        }
    }
}