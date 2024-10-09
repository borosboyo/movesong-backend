package hu.bme.aut.user.service

import hu.bme.aut.authhelper.JWTUtil
import hu.bme.aut.emailapi.api.EmailApi
import hu.bme.aut.emailapi.dto.EmailType
import hu.bme.aut.emailapi.dto.SendEmailReq
import hu.bme.aut.user.domain.Contact
import hu.bme.aut.user.domain.User
import hu.bme.aut.user.domain.token.Token
import hu.bme.aut.user.domain.token.TokenType
import hu.bme.aut.user.repository.ContactRepository
import hu.bme.aut.userapi.dto.exception.UserException
import hu.bme.aut.user.repository.TokenRepository
import hu.bme.aut.user.repository.UserRepository
import hu.bme.aut.userapi.dto.*
import hu.bme.aut.userapi.dto.req.*
import hu.bme.aut.userapi.dto.resp.*
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
@Import(JWTUtil::class)
open class UserService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val emailApi: EmailApi,
    private val jwtUtil: JWTUtil,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val contactRepository: ContactRepository
) {

    private final val USER_NOT_FOUND = "User not found!"

    @Transactional
    @Throws(UserException::class)
    open fun register(req: RegisterReq): RegisterResp {
        if (!UserServiceUtil.isUsernameValid(req.user.username)
            || !UserServiceUtil.isEmailValid(req.user.email)
            || !UserServiceUtil.isPasswordValid(req.user.password)
        ) {
            throw UserException("There is an error in the conditions of the registration fields!")
        }
        if (userRepository.existsByUsername(req.user.username)) {
            throw UserException("Error: Username is already taken!")
        }
        if (userRepository.existsByEmail(req.user.email)) {
            throw UserException("Error: Email is already taken!")
        }
        val user = User.fromDto(req.user)
        user.password = passwordEncoder.encode(user.password)
        val verificationToken: String = jwtUtil.generateToken(user)
        saveUserToken(user.id, verificationToken, TokenType.VERIFICATION)

        sendEmailToUserWithToken(user.getEmail(), EmailType.CONFIRM_REGISTRATION, verificationToken)

        return RegisterResp(User.toDto(userRepository.save(user)))
    }

    @Transactional
    @Throws(UserException::class)
    open fun login(req: LoginReq): LoginResp {
        var username = ""

        if (userRepository.existsByEmail(req.usernameOrEmail) && !userRepository.existsByUsername(req.usernameOrEmail)) {
            username = userRepository.findByEmail(req.usernameOrEmail).username
        }

        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, req.password)
            )
        } catch (e: AuthenticationException) {
            throw UserException(e.message!!)
        }
        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = authentication.principal as UserDetails

        val user = userRepository.findByUsername(userDetails.username)
        if (!user.enabled) {
            throw UserException("Your account has not yet been activated!")
        }

        val accessToken: String = jwtUtil.generateToken(user)
        saveUserToken(user.id, accessToken, TokenType.BEARER)
        return LoginResp(
            id = user.id,
            accessToken = accessToken,
            email = user.getEmail(),
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName
        )
    }

    @Transactional
    @Throws(UserException::class)
    open fun enable(req: EnableReq): EnableResp {
        try {
            val token: Token = tokenRepository.findByToken(req.token.trim())
            val user = userRepository.findById(token.userId)
            user.get().enabled = true
            val savedUserEntity = userRepository.save(user.get())
            val jwtToken: String = jwtUtil.generateToken(savedUserEntity)
            saveUserToken(savedUserEntity.id, jwtToken, TokenType.BEARER)
            return EnableResp(jwtToken)
        } catch (e: Exception) {
            throw UserException(e.message)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun resendEnable(req: ResendEnableReq): ResendEnableResp {
        if (userRepository.existsByEmail(req.email)) {
            val user = userRepository.findByEmail(req.email)
            if (user.enabled) {
                throw UserException("User is already enabled!")
            }
            val verificationToken: String = jwtUtil.generateToken(user)
            saveUserToken(user.id, verificationToken, TokenType.VERIFICATION)
            sendEmailToUserWithToken(user.getEmail(), EmailType.CONFIRM_REGISTRATION, verificationToken)
            return ResendEnableResp(true)
        } else {
            throw UserException(USER_NOT_FOUND)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun updatePassword(req: UpdatePasswordReq): UpdatePasswordResp {
        try {
            val user = userRepository.findByEmail(req.email)
            if (!passwordEncoder.matches(req.oldPassword, user.password)) {
                throw UserException("Old password is incorrect!")
            }
            if (!UserServiceUtil.isPasswordValid(req.newPassword)
            ) {
                throw UserException("There is an error in the conditions of the password field!")
            } else {
                user.password = passwordEncoder.encode(req.newPassword)
                userRepository.save(user)
                return UpdatePasswordResp(true)
            }
        } catch (e: Exception) {
            throw UserException(e.message)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun forgotPassword(req: ForgotPasswordReq): ForgotPasswordResp {
        if (userRepository.existsByEmail(req.email)) {
            val user = userRepository.findByEmail(req.email)
            val resetPasswordToken: String = jwtUtil.generateToken(user)
            saveUserToken(user.id, resetPasswordToken, TokenType.RESET_PASSWORD)
            sendEmailToUserWithToken(user.getEmail(), EmailType.RESET_PASSWORD, resetPasswordToken)
            return ForgotPasswordResp(true)
        } else {
            throw UserException(USER_NOT_FOUND)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun resendForgotPassword(req: ResendForgotPasswordReq): ResendForgotPasswordResp {
        if (userRepository.existsByEmail(req.email)) {
            val user = userRepository.findByEmail(req.email)
            val resetPasswordToken: String = jwtUtil.generateToken(user)
            saveUserToken(user.id, resetPasswordToken, TokenType.RESET_PASSWORD)
            sendEmailToUserWithToken(user.getEmail(), EmailType.RESET_PASSWORD, resetPasswordToken)
            return ResendForgotPasswordResp(true)
        } else {
            throw UserException(userNotFound)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun checkForgotPasswordToken(req: CheckForgotPasswordTokenReq): CheckForgotPasswordTokenResp {
        try {
            val token = tokenRepository.findByToken(req.token.trim())
            when {
                token.userId != userRepository.findByEmail(req.email).id -> {
                    throw UserException("Token is not valid for this user!")
                }
                token.expired -> {
                    throw UserException("Token is expired!")
                }
                token.revoked -> {
                    throw UserException("Token is revoked!")
                }
                token.tokenType != TokenType.RESET_PASSWORD -> {
                    throw UserException("Token is not a reset password token!")
                }
                else -> {
                    return CheckForgotPasswordTokenResp(true)
                }
            }
        } catch (e: Exception) {
            throw UserException(e.message)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun saveForgotPassword(req: SaveForgotPasswordReq): SaveForgotPasswordResp {
        try {
            val user = userRepository.findByEmail(req.email)
            user.password = passwordEncoder.encode(req.newPassword)
            userRepository.save(user)
            return SaveForgotPasswordResp(true)
        } catch (e: Exception) {
            throw UserException(e.message)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun delete(req: DeleteReq): DeleteResp {
        try {
            userRepository.deleteUserByEmail(req.email)
            tokenRepository.deleteAllByUserId(req.id)
            return DeleteResp(true)
        } catch (e: Exception) {
            throw UserException(e.message)
        }
    }

    @Transactional
    @Throws(UserException::class)
    open fun contact(req: ContactReq): ContactResp {
        try {
            val help = Contact(
                subject = req.subject,
                name = req.name,
                email = req.email,
                message = req.message
            )
            contactRepository.save(help)
            return ContactResp(true)
        } catch (e: Exception) {
            throw UserException(e.message)
        }
    }


    @Transactional
    @Throws(UserException::class)
    open fun findUserIdByEmail(req: FindUserIdByEmailReq): FindUserIdByEmailResp {
        try {
            val user = userRepository.findByEmail(req.email)
            return FindUserIdByEmailResp(user.id)
        } catch (e: Exception) {
            throw UserException(e.message)
        }
    }

    private fun sendEmailToUserWithToken(email: String, emailType: EmailType, token: String) {
        emailApi.sendEmail(
            SendEmailReq(
                userEmail = email,
                emailType = emailType,
                token = token
            )
        )
    }

    private fun saveUserToken(userId: Long, token: String, tokenType: TokenType) {
        val builtToken = Token(
            userId = userId,
            token = token,
            tokenType = tokenType,
            expired = false,
            revoked = false,
        )
        tokenRepository.save(builtToken)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserService::class.java)
    }
}