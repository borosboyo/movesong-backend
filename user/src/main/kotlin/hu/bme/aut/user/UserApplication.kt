package hu.bme.aut.user

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "User API",
        version = "1.0",
        description = "Documentation User API v1.0"
    )
)
open class UserApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(UserApplication::class.java, *args)
        }
    }
}
