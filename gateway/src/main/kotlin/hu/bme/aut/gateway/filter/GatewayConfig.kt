package hu.bme.aut.gateway.filter

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableHystrix
open class GatewayConfig(
    private val filter: AuthenticationFilter
) {

    @Bean
    open fun routes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
           .route(
               "organization"
           ) { r: PredicateSpec ->
               r.path("/organization/**")
                   .filters { f: GatewayFilterSpec -> f.filter(filter) }
                   .uri("lb://organization")
           }
            .route(
                "user"
            ) { r: PredicateSpec ->
                r.path("/user/**")
                    .filters { f: GatewayFilterSpec -> f.filter(filter) }
                    .uri("lb://user")
            }
            .build()
    }
}