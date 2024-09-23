package hu.bme.aut.emailapi.api

import hu.bme.aut.emailapi.dto.SendEmailReq
import hu.bme.aut.emailapi.dto.SendEmailResp
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "email")
@Api(tags = ["email"], value = "Email controller to handle all email related requests")
interface EmailApi {
    @PostMapping("/sendEmail")
    @ApiOperation(value = "Send email to a user", response = SendEmailResp::class, nickname = "sendEmail")
    fun sendEmail(@RequestBody req: SendEmailReq): ResponseEntity<SendEmailResp>
}