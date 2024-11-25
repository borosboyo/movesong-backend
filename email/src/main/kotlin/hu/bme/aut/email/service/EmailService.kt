package hu.bme.aut.email.service

import hu.bme.aut.email.domain.Email
import hu.bme.aut.emailapi.dto.EmailType
import hu.bme.aut.emailapi.dto.SendEmailReq
import hu.bme.aut.emailapi.dto.SendEmailResp
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.ISpringTemplateEngine
import java.io.StringWriter
import java.io.UnsupportedEncodingException

@Service
open class EmailService(
    private val emailSender: JavaMailSender,
    private val templateEngine: ISpringTemplateEngine
) {

    @Value("\${movesong.mail.from}")
    lateinit var mailFrom: String

    @Transactional
    @Throws(
        MessagingException::class,
        UnsupportedEncodingException::class
    )
    open fun sendEmail(req: SendEmailReq): SendEmailResp {
        try {
            val email: Email = this.createMail(
                req.userEmail,
                req.emailType,
                java.util.Map.of("token", req.token)
            )
            val emailMessage: MimeMessage = emailSender.createMimeMessage()
            val mailBuilder = MimeMessageHelper(emailMessage, true)
            mailBuilder.setFrom(email.mailFrom, "Movesong Team")
            mailBuilder.setTo(email.mailTo)
            mailBuilder.setSubject(email.mailSubject)
            mailBuilder.setText(email.mailContent, true)
            emailSender.send(emailMessage)
            return SendEmailResp(
                success = true
            )
        } catch (e: Exception) {
            return SendEmailResp(
                success = false
            )
        }
    }

    private fun createMail(to: String, emailType: EmailType, variables: Map<String, Any>): Email {
        val thymeleafContext = Context()
        thymeleafContext.setVariables(variables)
        val stringWriter = StringWriter()
        templateEngine.process(emailType.emailTemplate, thymeleafContext, stringWriter)
        val result = Email(
            mailTo = to,
            mailFrom = mailFrom,
            mailContent = stringWriter.toString(),
            mailSubject = emailType.emailSubject
        )
        return result
    }
}
