package hu.bme.aut.user

import hu.bme.aut.authhelper.JWTUtil
import hu.bme.aut.emailapi.api.EmailApi
import hu.bme.aut.shareapi.api.ShareApi
import hu.bme.aut.subscriptionapi.api.SubscriptionApi
import hu.bme.aut.transformapi.api.TransformApi
import hu.bme.aut.user.domain.Contact
import hu.bme.aut.user.domain.User
import hu.bme.aut.user.domain.token.Token
import hu.bme.aut.user.domain.token.TokenType
import hu.bme.aut.user.repository.ContactRepository
import hu.bme.aut.user.repository.TokenRepository
import hu.bme.aut.user.repository.UserRepository
import hu.bme.aut.user.service.UserService
import hu.bme.aut.userapi.dto.req.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var tokenRepository: TokenRepository
    @Mock
    private lateinit var emailApi: EmailApi
    @Mock
    private lateinit var shareApi: ShareApi
    @Mock
    private lateinit var transformApi: TransformApi
    @Mock
    private lateinit var subscriptionApi: SubscriptionApi
    @Mock
    private lateinit var jwtUtil: JWTUtil
    @Mock
    private lateinit var passwordEncoder: PasswordEncoder
    @Mock
    private lateinit var authenticationManager: AuthenticationManager
    @Mock
    private lateinit var contactRepository: ContactRepository
    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `register should create a new user and send a confirmation email`() {
        //given
        val req = RegisterReq("testuser", "Test", "User", "test@example.com", "TestPassword1234!")
        val user = User(username = req.username, firstName = req.firstName, lastName = req.lastName, email = req.email, password = req.password)

        //when
        `when`(userRepository.existsByUsername(req.username)).thenReturn(false)
        `when`(userRepository.existsByEmail(req.email)).thenReturn(false)
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)
        `when`(jwtUtil.generateNumericToken()).thenReturn("token123")
        `when`(passwordEncoder.encode(req.password)).thenReturn("encodedPassword")

        val response = userService.register(req)

        //then
        assertTrue(response.success)
        assertEquals("test@example.com", response.email)
        assertEquals("testuser", response.username)
    }

    @Test
    fun `login should authenticate user and return access token`() {
        val req = LoginReq("testuser", "password123")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = "test@example.com", password = "encodedPassword", enabled = true)
        val authToken = UsernamePasswordAuthenticationToken("testuser", "password123")
        val authentication = mock(Authentication::class.java)

        `when`(userRepository.findByUsername(req.usernameOrEmail)).thenReturn(user)
        `when`(authenticationManager.authenticate(authToken)).thenReturn(authentication)
        `when`(authentication.principal).thenReturn(user)
        `when`(jwtUtil.generateToken(user)).thenReturn("accessToken123")

        val response = userService.login(req)

        assertEquals(user.id, response.id)
        assertEquals("accessToken123", response.accessToken)
        assertEquals(user.getEmail(), response.email)
        assertEquals(user.username, response.username)
        assertEquals(user.firstName, response.firstName)
        assertEquals(user.lastName, response.lastName)
    }

    @Test
    fun `enable should activate a user account`() {
        val req = EnableReq("verificationToken")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = "test@example.com", password = "encodedPassword")

        val token = Token(
            token = req.token,
            tokenType = TokenType.BEARER,
            userId = user.id,
            expired = false,
            revoked = false
        )

        `when`(tokenRepository.findByToken(req.token.trim())).thenReturn(token)
        `when`(userRepository.findById(user.id)).thenReturn(Optional.of(user))

        val response = userService.enable(req)

        assertTrue(response.success)
        assertTrue(user.enabled)
    }

    @Test
    fun `resendEnable should send a new confirmation email to the user`() {
        val req = ResendEnableReq("test@example.com")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = "req.email", password = "encodedPassword", enabled = false)

        `when`(userRepository.existsByEmail(req.email)).thenReturn(true)
        `when`(userRepository.findByEmail(req.email)).thenReturn(user)
        `when`(jwtUtil.generateNumericToken()).thenReturn("newToken123")

        val response = userService.resendEnable(req)

        assertTrue(response.success)
    }

    @Test
    fun `updatePassword should change the user's password`() {
        val req = UpdatePasswordReq("test@example.com", "TestPassword1234!", "TestPassword1234!")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = "req.email", password = "encodedPassword")

        `when`(userRepository.findByEmail(req.email)).thenReturn(user)
        `when`(passwordEncoder.matches(req.oldPassword, user.password)).thenReturn(true)
        `when`(passwordEncoder.encode(req.newPassword)).thenReturn("encodedNewPassword")

        val response = userService.updatePassword(req)

        assertTrue(response.success)
        assertEquals("encodedNewPassword", user.password)

        verify(userRepository).save(user)
    }

    @Test
    fun `forgotPassword should send a reset password email`() {
        val req = ForgotPasswordReq("test@example.com")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = req.email, password = "encodedPassword")

        `when`(userRepository.existsByEmail(req.email)).thenReturn(true)
        `when`(userRepository.findByEmail(req.email)).thenReturn(user)
        `when`(jwtUtil.generateNumericToken()).thenReturn("resetToken123")

        val response = userService.forgotPassword(req)

        assertTrue(response.success)
    }

    @Test
    fun `delete should remove the user from the repository`() {
        val req = DeleteReq("test@example.com", "sub_123")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = req.email, password = "encodedPassword")

        `when`(userRepository.findByEmail(req.email)).thenReturn(user)

        val response = userService.delete(req)

        assertTrue(response.success)
        verify(tokenRepository).deleteAllByUserId(user.id)
        verify(userRepository).deleteUserByEmail(req.email)
    }

    @Test
    fun `findUserIdByEmail should return the user's ID`() {
        val req = FindUserIdByEmailReq("test@example.com")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = req.email, password = "encodedPassword")

        `when`(userRepository.findByEmail(req.email)).thenReturn(user)

        val response = userService.findUserIdByEmail(req)

        assertEquals(user.id, response.userId)
    }

    @Test
    fun `saveForgotPassword should update the user's password`() {
        val req = SaveForgotPasswordReq("test@example.com", "newPassword123")
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = req.email, password = "encodedOldPassword")

        `when`(userRepository.findByEmail(req.email)).thenReturn(user)
        `when`(passwordEncoder.encode(req.newPassword)).thenReturn("encodedNewPassword")

        val response = userService.saveForgotPassword(req)

        assertTrue(response.success)
        assertEquals("encodedNewPassword", user.password)
        verify(userRepository).save(user)
    }

    @Test
    fun `checkForgotPasswordToken should validate the token`() {
        val req = CheckForgotPasswordTokenReq("resetToken123", "test@example.com")
        val token = Token(token = req.token, tokenType = TokenType.RESET_PASSWORD, userId = 1L, expired = false, revoked = false)
        val user = User(username = "testuser", firstName = "Test", lastName = "User", email = req.email, password = "encodedPassword", id = 1L)

        `when`(tokenRepository.findByToken(req.token.trim())).thenReturn(token)
        `when`(userRepository.findByEmail(req.email)).thenReturn(user)

        val response = userService.checkForgotPasswordToken(req)

        assertTrue(response.success)
    }

    @Test
    fun `contact should save the contact request and send an email`() {
        val req = ContactReq("subject", "name", "test@example.com", "message")
        val contact = Contact(subject = req.subject, name = req.name, email = req.email, message = req.message)

        val response = userService.contact(req)

        assertTrue(response.success)
    }
}
