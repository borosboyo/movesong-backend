package hu.bme.aut.userapi.api

import hu.bme.aut.userapi.dto.exception.UserException
import hu.bme.aut.userapi.dto.req.*
import hu.bme.aut.userapi.dto.resp.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "user")
@Api(tags = ["user"], value = "User controller to handle all user related requests")
interface UserApi {
    @PostMapping(value = ["/register"])
    @ApiOperation(value = "Register a user", response = RegisterResp::class, nickname = "register")
    @Throws(UserException::class)
    fun register(@RequestBody authRequest: RegisterReq): ResponseEntity<RegisterResp>

    @PostMapping(value = ["/login"])
    @ApiOperation(value = "Login the user", response = LoginResp::class, nickname = "login")
    @Throws(UserException::class)
    fun login(@RequestBody authRequest: LoginReq): ResponseEntity<LoginResp>

    @PostMapping(value = ["/enable"])
    @ApiOperation(value = "Enable the user", response = EnableResp::class, nickname = "enable")
    @Throws(UserException::class)
    fun enable(@RequestBody enableReq: EnableReq): ResponseEntity<EnableResp>

    @PostMapping(value = ["/resendEnable"])
    @ApiOperation(value = "Resend the enable email to the user", response = ResendEnableResp::class, nickname = "resendEnable")
    @Throws(UserException::class)
    fun resendEnable(@RequestBody resendEnableReq: ResendEnableReq): ResponseEntity<ResendEnableResp>

    @PostMapping(value = ["/updatePassword"])
    @ApiOperation(value = "Update the password of the user", response = UpdatePasswordResp::class, nickname = "updatePassword")
    @Throws(UserException::class)
    fun updatePassword(@RequestBody updateReq: UpdatePasswordReq): ResponseEntity<UpdatePasswordResp>

    @PostMapping(value = ["/forgotPassword"])
    @ApiOperation(value = "Forgot the password of the user", response = ForgotPasswordResp::class, nickname = "forgotPassword")
    @Throws(UserException::class)
    fun forgotPassword(@RequestBody forgotReq: ForgotPasswordReq): ResponseEntity<ForgotPasswordResp>

    @PostMapping(value = ["/resendForgotPassword"])
    @ApiOperation(value = "Resend the forgot password email to the user", response = ResendForgotPasswordResp::class, nickname = "resendForgotPassword")
    @Throws(UserException::class)
    fun resendForgotPassword(@RequestBody resendForgotReq: ResendForgotPasswordReq): ResponseEntity<ResendForgotPasswordResp>

    @PostMapping(value = ["/saveForgotPassword"])
    @ApiOperation(value = "Save the forgot password of the user", response = SaveForgotPasswordResp::class, nickname = "saveForgotPassword")
    @Throws(UserException::class)
    fun saveForgotPassword(@RequestBody saveForgotReq: SaveForgotPasswordReq): ResponseEntity<SaveForgotPasswordResp>

    @PostMapping(value = ["/delete"])
    @ApiOperation(value = "Delete the user", response = DeleteResp::class, nickname = "delete")
    @Throws(UserException::class)
    fun delete(@RequestBody deleteReq: DeleteReq): ResponseEntity<DeleteResp>

    @PostMapping(value = ["/contact"])
    @ApiOperation(value = "Contact request from the user", response = ContactResp::class, nickname = "contact")
    @Throws(UserException::class)
    fun contact(@RequestBody contactReq: ContactReq): ResponseEntity<ContactResp>
}