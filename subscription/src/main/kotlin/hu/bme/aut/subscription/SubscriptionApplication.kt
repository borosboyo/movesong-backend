package hu.bme.aut.subscription

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@OpenAPIDefinition(
    info = Info(
        title = "Subscription API",
        version = "1.0",
        description = "Documentation Subscription API v1.0"
    )
)
open class SubscriptionApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SubscriptionApplication::class.java, *args)
        }
    }
}
