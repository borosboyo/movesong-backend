package hu.bme.aut.share

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "Share API",
        version = "1.0",
        description = "Documentation Share API v1.0"
    )
)
open class ShareApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ShareApplication::class.java, *args)
        }
    }
}
