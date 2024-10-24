package hu.bme.aut.department

import hu.bme.aut.department.model.Department
import hu.bme.aut.department.repository.DepartmentRepository
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
        title = "Department API",
        version = "1.0",
        description = "Documentation Department API v1.0"
    )
)
open class DepartmentApplication {
    @Bean
    open fun repository(): DepartmentRepository {
        val repository = DepartmentRepository()
        repository.add(Department(1L, "Development"))
        repository.add(Department(1L, "Operations"))
        repository.add(Department(2L, "Development"))
        repository.add(Department(2L, "Operations"))
        return repository
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DepartmentApplication::class.java, *args)
        }
    }
}
