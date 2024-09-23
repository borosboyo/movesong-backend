package hu.bme.aut.email

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
    info = Info(
        title = "Email API",
        version = "1.0",
        description = "Documentation Email API v1.0"
    )
)
open class EmailApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EmailApplication::class.java, *args)
        }
    }
}
