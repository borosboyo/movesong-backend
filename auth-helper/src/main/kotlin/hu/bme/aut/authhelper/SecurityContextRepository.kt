package hu.bme.aut.authhelper

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Import(AuthenticationManager::class)
class SecurityContextRepository(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
) : ServerSecurityContextRepository {

    override fun save(swe: ServerWebExchange, sc: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(swe: ServerWebExchange): Mono<SecurityContext> {
        LOGGER.info("SCR Loading security context")
        val request = swe.request
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val authToken = authHeader.substring(7)
            val username = jwtUtil.extractUsername(authToken)
            LOGGER.info("SCR Username: $username")
            LOGGER.info("SCR Token: $authToken")
            val auth: Authentication = UsernamePasswordAuthenticationToken(
                username, // Use the extracted username
                authToken // Use the token as credentials
            )
            LOGGER.info(auth.toString());
            authenticationManager.authenticate(auth).map { authentication ->
                SecurityContextImpl(authentication)
            }
        } else {
            Mono.empty()
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SecurityContextRepository::class.java)
    }
}