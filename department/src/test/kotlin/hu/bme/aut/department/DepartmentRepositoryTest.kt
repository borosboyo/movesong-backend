package hu.bme.aut.department

import hu.bme.aut.department.model.Department
import hu.bme.aut.department.repository.DepartmentRepository
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.util.Assert

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DepartmentRepositoryTest {
    @Test
    @Order(1)
    fun testAddDepartment() {
        var department = Department(1L, "Test")
        department = repository.add(department)
        Assert.notNull(department, "Department is null.")
        Assert.isTrue(department.id == 1L, "Department bad id.")
    }

    @Test
    @Order(2)
    fun testFindAll() {
        val departments = repository.findAll()
        Assert.isTrue(departments.size == 1, "Departments size is wrong.")
        Assert.isTrue(departments[0].id == 1L, "Department bad id.")
    }

    @Test
    @Order(3)
    fun testFindByOrganization() {
        val departments = repository.findByOrganization(1L)
        Assert.isTrue(departments.size == 1, "Departments size is wrong.")
        Assert.isTrue(departments[0].id == 1L, "Department bad id.")
    }

    @Test
    @Order(4)
    fun testFindById() {
        val department = repository.findById(1L)
        Assert.notNull(department, "Department not found.")
        Assert.isTrue(department.id == 1L, "Department bad id.")
    }

    companion object {
        private val repository = DepartmentRepository()
    }
}
