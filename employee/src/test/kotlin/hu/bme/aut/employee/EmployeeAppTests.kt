package hu.bme.aut.employee

import hu.bme.aut.employee.model.Employee
import org.instancio.Instancio
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.cloud.discovery.enabled=false", "spring.cloud.config.discovery.enabled=false"]
)
class EmployeeAppTests {
    @Autowired
    var restTemplate: TestRestTemplate? = null
    @Test
    fun findAll() {
        val employees = restTemplate!!.getForObject("/", Array<Employee>::class.java)
        Assertions.assertTrue(employees.size > 0)
    }

    @Test
    fun findById() {
        val employee = restTemplate!!.getForObject("/{id}", Employee::class.java, 1L)
        Assertions.assertNotNull(employee)
        Assertions.assertNotNull(employee.id)
        Assertions.assertNotNull(employee.name)
        Assertions.assertEquals(1L, employee.id)
    }

    @Test
    fun findByOrganization() {
        val employees = restTemplate!!.getForObject("/organization/{organizationId}", Array<Employee>::class.java, 1L)
        Assertions.assertTrue(employees.size > 0)
    }

    @Test
    fun findByDepartment() {
        val employees = restTemplate!!.getForObject("/department/{departmentId}", Array<Employee>::class.java, 1L)
        Assertions.assertTrue(employees.size > 0)
    }

    @Test
    fun add() {
        var employee = Instancio.create(Employee::class.java)
        employee = restTemplate!!.postForObject("/", employee, Employee::class.java)
        Assertions.assertNotNull(employee)
        Assertions.assertNotNull(employee.id)
        Assertions.assertNotNull(employee.name)
    }
}
