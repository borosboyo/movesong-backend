package hu.bme.aut.organization

import hu.bme.aut.organization.model.Organization
import hu.bme.aut.organization.repository.OrganizationRepository
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
    info = Info(
        title = "Organization API",
        version = "1.0",
        description = "Documentation Organization API v1.0"
    )
)
open class OrganizationApplication {
    @Bean
    open fun repository(): OrganizationRepository {
        val repository = OrganizationRepository()
        repository.add(Organization("Microsoft", "Redmond, Washington, USA"))
        repository.add(Organization("Oracle", "Redwood City, California, USA"))
        return repository
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(OrganizationApplication::class.java, *args)
        }
    }
}
