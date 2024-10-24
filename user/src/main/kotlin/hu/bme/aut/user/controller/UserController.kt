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

    override fun register(@RequestBody req: RegisterReq): ResponseEntity<RegisterResp> {
        return ResponseEntity.ok(userService.register(req))
    }

    override fun login(@RequestBody req: LoginReq): ResponseEntity<LoginResp> {
        return ResponseEntity.ok(userService.login(req))
    }

    override fun enable(req: EnableReq): ResponseEntity<EnableResp> {
        return ResponseEntity.ok(userService.enable(req))
    }

    override fun resendEnable(req: ResendEnableReq): ResponseEntity<ResendEnableResp> {
        return ResponseEntity.ok(userService.resendEnable(req))
    }

    override fun updatePassword(req: UpdatePasswordReq): ResponseEntity<UpdatePasswordResp> {
        return ResponseEntity.ok(userService.updatePassword(req))
    }

    override fun forgotPassword(req: ForgotPasswordReq): ResponseEntity<ForgotPasswordResp> {
        return ResponseEntity.ok(userService.forgotPassword(req))
    }

    override fun resendForgotPassword(req: ResendForgotPasswordReq): ResponseEntity<ResendForgotPasswordResp> {
        return ResponseEntity.ok(userService.resendForgotPassword(req))
    }

    override fun checkForgotPasswordToken(req: CheckForgotPasswordTokenReq): ResponseEntity<CheckForgotPasswordTokenResp> {
        return ResponseEntity.ok(userService.checkForgotPasswordToken(req))
    }

    override fun saveForgotPassword(req: SaveForgotPasswordReq): ResponseEntity<SaveForgotPasswordResp> {
        return ResponseEntity.ok(userService.saveForgotPassword(req))
    }

    override fun delete(req: DeleteReq): ResponseEntity<DeleteResp> {
        return ResponseEntity.ok(userService.delete(req))
    }

    override fun contact(req: ContactReq): ResponseEntity<ContactResp> {
        return ResponseEntity.ok(userService.contact(req))
    }

    override fun findUserIdByEmail(req: FindUserIdByEmailReq): ResponseEntity<FindUserIdByEmailResp> {
        return ResponseEntity.ok(userService.findUserIdByEmail(req))
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserController::class.java)
    }
}