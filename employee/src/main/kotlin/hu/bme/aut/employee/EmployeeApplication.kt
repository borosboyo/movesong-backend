package hu.bme.aut.employee

import hu.bme.aut.employee.model.Employee
import hu.bme.aut.employee.repository.EmployeeRepository
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "Employee API",
        version = "1.0",
        description = "Documentation Employee API v1.0"
    )
)
open class EmployeeApplication {
    @Bean
    open fun repository(): EmployeeRepository {
        val repository = EmployeeRepository()
        repository.add(Employee(1L, 1L, "John Smith", 34, "Analyst"))
        repository.add(Employee(1L, 1L, "Darren Hamilton", 37, "Manager"))
        repository.add(Employee(1L, 1L, "Tom Scott", 26, "Developer"))
        repository.add(Employee(1L, 2L, "Anna London", 39, "Analyst"))
        repository.add(Employee(1L, 2L, "Patrick Dempsey", 27, "Developer"))
        repository.add(Employee(2L, 3L, "Kevin Price", 38, "Developer"))
        repository.add(Employee(2L, 3L, "Ian Scott", 34, "Developer"))
        repository.add(Employee(2L, 3L, "Andrew Campton", 30, "Manager"))
        repository.add(Employee(2L, 4L, "Steve Franklin", 25, "Developer"))
        repository.add(Employee(2L, 4L, "Elisabeth Smith", 30, "Developer"))
        return repository
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EmployeeApplication::class.java, *args)
        }
    }
}
