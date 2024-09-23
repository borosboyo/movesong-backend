package hu.bme.aut.user.controller

import hu.bme.aut.user.service.UserService
import hu.bme.aut.userapi.api.UserApi
import hu.bme.aut.userapi.dto.req.*
import hu.bme.aut.userapi.dto.resp.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) : UserApi {

    override fun registerTest(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(userService.registerTest(authRequest))
    }

    override fun register(@RequestBody authRequest: RegisterReq): ResponseEntity<RegisterResp> {
        return ResponseEntity.ok(userService.register(authRequest))
    }

    override fun login(@RequestBody authRequest: LoginReq): ResponseEntity<LoginResp> {
        return ResponseEntity.ok(userService.login(authRequest))
    }

    override fun enable(enableReq: EnableReq): ResponseEntity<EnableResp> {
        return ResponseEntity.ok(userService.enable(enableReq))
    }

    override fun resendEnable(resendEnableReq: ResendEnableReq): ResponseEntity<ResendEnableResp> {
        return ResponseEntity.ok(userService.resendEnable(resendEnableReq))
    }

    override fun updatePassword(updateReq: UpdatePasswordReq): ResponseEntity<UpdatePasswordResp> {
        return ResponseEntity.ok(userService.updatePassword(updateReq))
    }

    override fun forgotPassword(forgotReq: ForgotPasswordReq): ResponseEntity<ForgotPasswordResp> {
        return ResponseEntity.ok(userService.forgotPassword(forgotReq))
    }

    override fun resendForgotPassword(resendForgotReq: ResendForgotPasswordReq): ResponseEntity<ResendForgotPasswordResp> {
        return ResponseEntity.ok(userService.resendForgotPassword(resendForgotReq))
    }

    override fun saveForgotPassword(saveForgotReq: SaveForgotPasswordReq): ResponseEntity<SaveForgotPasswordResp> {
        return ResponseEntity.ok(userService.saveForgotPassword(saveForgotReq))
    }

    override fun delete(deleteReq: DeleteReq): ResponseEntity<DeleteResp> {
        return ResponseEntity.ok(userService.delete(deleteReq))
    }

    override fun contact(contactReq: ContactReq): ResponseEntity<ContactResp> {
        return ResponseEntity.ok(userService.contact(contactReq))
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserController::class.java)
    }
}