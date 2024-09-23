package hu.bme.aut.email.controller

import hu.bme.aut.email.service.EmailService
import hu.bme.aut.emailapi.dto.SendEmailReq
import hu.bme.aut.emailapi.dto.SendEmailResp
import hu.bme.aut.emailapi.api.EmailApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/email")
class EmailController(
    private val emailService: EmailService
) : EmailApi {
    override fun sendEmail(@RequestBody req: SendEmailReq): ResponseEntity<SendEmailResp> {
        return ResponseEntity.ok(emailService.sendEmail(req))
    }
}