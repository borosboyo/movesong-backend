package hu.bme.aut.email

import hu.bme.aut.email.service.EmailService
import hu.bme.aut.emailapi.dto.EmailType
import hu.bme.aut.emailapi.dto.SendEmailReq
import hu.bme.aut.emailapi.dto.SendEmailResp
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mail.javamail.JavaMailSender
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.ISpringTemplateEngine
import java.io.StringWriter

@ExtendWith(MockitoExtension::class)
class EmailServiceTest {

    private val emailSender = mock(JavaMailSender::class.java)
    private val templateEngine = mock(ISpringTemplateEngine::class.java)
    private val emailService = EmailService(emailSender, templateEngine).apply {
        mailFrom = "no-reply@movesong.com"
    }

    @Test
    fun `test sendEmail success`() {
        val req = SendEmailReq(
            userEmail = "test@example.com",
            emailType = EmailType.CONFIRM_REGISTRATION,
            token = "sample-token"
        )
        
        val mimeMessage = mock(MimeMessage::class.java)

        `when`(emailSender.createMimeMessage()).thenReturn(mimeMessage)
        `when`(templateEngine.process(eq(req.emailType.emailTemplate), any(Context::class.java), any(StringWriter::class.java)))
            .thenAnswer {
                (it.arguments[2] as StringWriter).write("Generated email content")
            }

        val response: SendEmailResp = emailService.sendEmail(req)

        assertEquals(true, response.success)
        verify(emailSender).send(mimeMessage)
    }
}
