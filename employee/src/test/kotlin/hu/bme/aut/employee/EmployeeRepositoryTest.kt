package hu.bme.aut.employee

import hu.bme.aut.employee.model.Employee
import hu.bme.aut.employee.repository.EmployeeRepository
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.util.Assert

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EmployeeRepositoryTest {
    @Test
    @Order(1)
    fun testAddEmployee() {
        var employee = Employee(1L, 1L, "Test Test", 100, "Test")
        employee = repository.add(employee)
        Assert.notNull(employee, "Employee is null.")
        Assert.isTrue(employee.id == 1L, "Employee bad id.")
    }

    @Test
    @Order(2)
    fun testFindAll() {
        val employees = repository.findAll()
        Assert.isTrue(employees.size == 1, "Employees size is wrong.")
        Assert.isTrue(employees[0].id == 1L, "Employee bad id.")
    }

    @Test
    @Order(3)
    fun testFindByDepartment() {
        val employees = repository.findByDepartment(1L)
        Assert.isTrue(employees.size == 1, "Employees size is wrong.")
        Assert.isTrue(employees[0].id == 1L, "Employee bad id.")
    }

    @Test
    @Order(4)
    fun testFindByOrganization() {
        val employees = repository.findByOrganization(1L)
        Assert.isTrue(employees.size == 1, "Employees size is wrong.")
        Assert.isTrue(employees[0].id == 1L, "Employee bad id.")
    }

    @Test
    @Order(5)
    fun testFindById() {
        val employee = repository.findById(1L)
        Assert.notNull(employee, "Employee not found.")
        Assert.isTrue(employee.id == 1L, "Employee bad id.")
    }

    companion object {
        private val repository = EmployeeRepository()
    }
}
