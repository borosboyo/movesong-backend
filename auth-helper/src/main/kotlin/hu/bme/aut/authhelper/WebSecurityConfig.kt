package hu.bme.aut.authhelper

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Import(AuthenticationManager::class, SecurityContextRepository::class)
class WebSecurityConfig(
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository
) {

    private val urls: Array<String> = arrayOf(
        "/user/register",
    )

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .exceptionHandling { exceptionHandlingSpec ->
                exceptionHandlingSpec.authenticationEntryPoint { swe: ServerWebExchange, e: AuthenticationException? ->
                    Mono.fromRunnable {
                        swe.response.statusCode = HttpStatus.UNAUTHORIZED
                        if (e != null) {
                            Mono.error<Any>(e)
                        }
                    }
                }
                exceptionHandlingSpec.accessDeniedHandler { swe: ServerWebExchange, e: AccessDeniedException? ->
                    Mono.fromRunnable {
                        swe.response.statusCode = HttpStatus.FORBIDDEN
                        if (e != null) {
                            Mono.error<Any>(e)
                        }
                    }
                }
            }
            .csrf { csrfSpec -> csrfSpec.disable() }
            .cors { corsSpec -> corsSpec.disable() }
            .formLogin { formLoginSpec -> formLoginSpec.disable() }
            .httpBasic { httpBasicSpec -> httpBasicSpec.disable() }
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange { exchangeSpec ->
                exchangeSpec.pathMatchers(HttpMethod.OPTIONS).permitAll()
                exchangeSpec.pathMatchers(*urls).permitAll()
                exchangeSpec.anyExchange().authenticated()
            }
            .build()
    }
}
