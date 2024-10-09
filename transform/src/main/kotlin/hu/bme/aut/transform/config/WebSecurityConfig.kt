package hu.bme.aut.transform.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrfSpec -> csrfSpec.disable() }
            .cors { corsSpec -> corsSpec.disable() }
            .formLogin { formLoginSpec -> formLoginSpec.disable() }
            .httpBasic { httpBasicSpec -> httpBasicSpec.disable() }
            .authorizeHttpRequests { exchangeSpec ->
                exchangeSpec.anyRequest().permitAll()
            }
            .build()
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins =
            listOf("http://localhost:19006", "http://127.0.0.1:19006", "http://[::1]:19006")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowCredentials = true
        configuration.allowedHeaders =
            listOf("Authorization", "Cache-Control", "Content-Type", "X-Requested-With")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}