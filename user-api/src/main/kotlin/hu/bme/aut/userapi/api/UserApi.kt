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
    fun register(@RequestBody req: RegisterReq): ResponseEntity<RegisterResp>

    @PostMapping(value = ["/login"])
    @ApiOperation(value = "Login the user", response = LoginResp::class, nickname = "login")
    @Throws(UserException::class)
    fun login(@RequestBody req: LoginReq): ResponseEntity<LoginResp>

    @PostMapping(value = ["/enable"])
    @ApiOperation(value = "Enable the user", response = EnableResp::class, nickname = "enable")
    @Throws(UserException::class)
    fun enable(@RequestBody req: EnableReq): ResponseEntity<EnableResp>

    @PostMapping(value = ["/resendEnable"])
    @ApiOperation(value = "Resend the enable email to the user", response = ResendEnableResp::class, nickname = "resendEnable")
    @Throws(UserException::class)
    fun resendEnable(@RequestBody req: ResendEnableReq): ResponseEntity<ResendEnableResp>

    @PostMapping(value = ["/updatePassword"])
    @ApiOperation(value = "Update the password of the user", response = UpdatePasswordResp::class, nickname = "updatePassword")
    @Throws(UserException::class)
    fun updatePassword(@RequestBody req: UpdatePasswordReq): ResponseEntity<UpdatePasswordResp>

    @PostMapping(value = ["/forgotPassword"])
    @ApiOperation(value = "Forgot the password of the user", response = ForgotPasswordResp::class, nickname = "forgotPassword")
    @Throws(UserException::class)
    fun forgotPassword(@RequestBody req: ForgotPasswordReq): ResponseEntity<ForgotPasswordResp>

    @PostMapping(value = ["/checkForgotPasswordToken"])
    @ApiOperation(value = "Check the forgot password token of the user", response = CheckForgotPasswordTokenResp::class, nickname = "checkForgotPasswordToken")
    @Throws(UserException::class)
    fun checkForgotPasswordToken(@RequestBody req: CheckForgotPasswordTokenReq): ResponseEntity<CheckForgotPasswordTokenResp>

    @PostMapping(value = ["/saveForgotPassword"])
    @ApiOperation(value = "Save the forgot password of the user", response = SaveForgotPasswordResp::class, nickname = "saveForgotPassword")
    @Throws(UserException::class)
    fun saveForgotPassword(@RequestBody req: SaveForgotPasswordReq): ResponseEntity<SaveForgotPasswordResp>

    @PostMapping(value = ["/delete"])
    @ApiOperation(value = "Delete the user", response = DeleteResp::class, nickname = "delete")
    @Throws(UserException::class)
    fun delete(@RequestBody req: DeleteReq): ResponseEntity<DeleteResp>

    @PostMapping(value = ["/contact"])
    @ApiOperation(value = "Contact request from the user", response = ContactResp::class, nickname = "contact")
    @Throws(UserException::class)
    fun contact(@RequestBody req: ContactReq): ResponseEntity<ContactResp>

    @PostMapping(value = ["/findUserIdByEmail"])
    @ApiOperation(value = "Find the user id by email", response = FindUserIdByEmailResp::class, nickname = "findUserIdByEmail")
    @Throws(UserException::class)
    fun findUserIdByEmail(@RequestBody req: FindUserIdByEmailReq): ResponseEntity<FindUserIdByEmailResp>
}