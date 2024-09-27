package hu.bme.aut.gateway.filter

import hu.bme.aut.authhelper.JWTUtil
import io.jsonwebtoken.Claims
import org.slf4j.LoggerFactory
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RefreshScope
@Component
@Import(JWTUtil::class)
open class AuthenticationFilter(
    private var routerValidator: RouterValidator,
    private var jwtUtil: JWTUtil,
): GatewayFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request: ServerHttpRequest = exchange.request
        LOGGER.info(routerValidator.isSecured.test(request).toString())
        LOGGER.info("--------------------------------------")
        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED)
            }
            val authHeader = this.getAuthHeader(request)
            val jwt = authHeader.substring(7)
            if (!jwtUtil.isTokenValid(jwt)) {
                return this.onError(exchange, HttpStatus.FORBIDDEN)
            }

            this.updateRequest(exchange, jwt)
        }
        return chain.filter(exchange)
    }

    private fun onError(exchange: ServerWebExchange, httpStatus: HttpStatus): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.setStatusCode(httpStatus)
        return response.setComplete()
    }

    private fun getAuthHeader(request: ServerHttpRequest): String {
        return request.headers.getOrEmpty("Authorization")[0]
    }

    private fun isAuthMissing(request: ServerHttpRequest): Boolean {
        return !request.headers.containsKey("Authorization")
    }

    private fun updateRequest(exchange: ServerWebExchange, token: String) {
        val claims: Claims = jwtUtil.extractAllClaims(token)
        exchange.request.mutate()
            .header("username", java.lang.String.valueOf(claims["username"]))
            .build()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AuthenticationFilter::class.java)
    }
}