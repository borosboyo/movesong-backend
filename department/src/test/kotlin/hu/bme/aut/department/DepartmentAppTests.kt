package hu.bme.aut.department

import hu.bme.aut.department.client.EmployeeClient
import hu.bme.aut.department.model.Department
import hu.bme.aut.department.model.Employee
import org.instancio.Instancio
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.cloud.discovery.enabled=false", "spring.cloud.config.discovery.enabled=false"]
)
class DepartmentAppTests {
    @Autowired
    var restTemplate: TestRestTemplate? = null

    @MockBean
    var employeeClient: EmployeeClient? = null
    @Test
    fun findAll() {
        val departments = restTemplate!!.getForObject("/", Array<Department>::class.java)
        Assertions.assertTrue(departments.size > 0)
    }

    @Test
    fun findById() {
        val department = restTemplate!!.getForObject("/{id}", Department::class.java, 1L)
        Assertions.assertNotNull(department)
        Assertions.assertNotNull(department.id)
        Assertions.assertNotNull(department.name)
        Assertions.assertEquals(1L, department.id)
    }

    @Test
    fun findByOrganization() {
        val departments =
            restTemplate!!.getForObject("/organization/{organizationId}", Array<Department>::class.java, 1L)
        Assertions.assertTrue(departments.size > 0)
    }

    @Test
    fun findByOrganizationWithEmployees() {
        Mockito.`when`(employeeClient!!.findByDepartment(Mockito.anyLong()))
            .thenReturn(Instancio.ofList(Employee::class.java).create())
        val departments = restTemplate!!.getForObject(
            "/organization/{organizationId}/with-employees",
            Array<Department>::class.java,
            1L
        )
        Assertions.assertTrue(departments.size > 0)
    }

    @Test
    fun add() {
        var department = Instancio.create(
            Department::class.java
        )
        department = restTemplate!!.postForObject("/", department, Department::class.java)
        Assertions.assertNotNull(department)
        Assertions.assertNotNull(department.id)
        Assertions.assertNotNull(department.name)
    }
}
