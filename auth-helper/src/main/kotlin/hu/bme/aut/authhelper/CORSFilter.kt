package hu.bme.aut.authhelper

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
open class CORSFilter : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedOrigins("http://localhost:5173")
            .allowedHeaders("*")
            .allowedMethods("*")
            .exposedHeaders(HttpHeaders.SET_COOKIE)
    }

    @Bean
    open fun corsWebFilter(): CorsWebFilter {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")
        corsConfiguration.allowedOrigins = mutableListOf("http://localhost:5173")
        corsConfiguration.allowCredentials = true
        corsConfiguration.addExposedHeader(HttpHeaders.SET_COOKIE)
        val corsConfigurationSource = UrlBasedCorsConfigurationSource()
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)
        return CorsWebFilter(corsConfigurationSource)
    }
}