package hu.bme.aut.gateway.filter

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
open class RouterValidator {
    companion object {
        val openApiEndpoints: List<String> = listOf(
            "/user/register",
        )
    }

    var isSecured: Predicate<ServerHttpRequest> = Predicate<ServerHttpRequest> { request ->
        openApiEndpoints
            .stream()
            .noneMatch { uri: String -> request.uri.getPath().contains(uri) }
    }
}