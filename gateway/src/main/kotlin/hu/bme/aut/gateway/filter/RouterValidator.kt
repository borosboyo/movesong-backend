package hu.bme.aut.gateway.filter

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
open class RouterValidator {
    companion object {
        val openApiEndpoints: List<String> = listOf(
            "/user/register",
            "/user/enable",
            "/user/resendEnable",
            "/user/login",
            "/user/contact",
            "/user/forgotPassword",
            "/user/resendForgotPassword",
            "/user/checkForgotPasswordToken",
            "/email/sendEmail",
            "/share/getShareById",
        )
    }

    var isSecured: Predicate<ServerHttpRequest> = Predicate<ServerHttpRequest> { request ->
        openApiEndpoints
            .stream()
            .noneMatch { uri: String -> request.uri.getPath().contains(uri) }
    }
}