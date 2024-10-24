package hu.bme.aut.authhelper

import org.springframework.context.annotation.Import
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
@Import(JWTUtil::class)
class AuthenticationManager(
    private val jwtUtil: JWTUtil
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()
        println("TOKEN:" + authToken)
        try {
            if (!jwtUtil.isTokenValid(authToken)) {
                return Mono.empty()
            }
            val username = jwtUtil.extractUsername(authToken)
            println("USER NAME: " + username)
            return Mono.just(
                UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    listOf(SimpleGrantedAuthority("ROLE_USER")) // Add a sample role here
                )
            )
        } catch (e: Exception) {
            return Mono.empty()
        }
    }
}