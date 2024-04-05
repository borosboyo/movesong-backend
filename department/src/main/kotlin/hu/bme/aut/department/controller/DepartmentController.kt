package hu.bme.aut.department.controller

import hu.bme.aut.department.client.EmployeeClient
import hu.bme.aut.department.model.Department
import hu.bme.aut.department.repository.DepartmentRepository
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer

@RestController
class DepartmentController(var repository: DepartmentRepository, var employeeClient: EmployeeClient) {
    @PostMapping("/")
    fun add(@RequestBody department: Department): Department? {
        LOGGER.info("Department add: {}", department)
        return repository.add(department)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): Department? {
        LOGGER.info("Department find: id={}", id)
        return repository.findById(id)
    }

    @GetMapping("/")
    fun findAll(): List<Department?>? {
        LOGGER.info("Department find")
        return repository.findAll()
    }

    @GetMapping("/organization/{organizationId}")
    fun findByOrganization(@PathVariable("organizationId") organizationId: Long): List<Department?>? {
        LOGGER.info("Department find: organizationId={}", organizationId)
        return repository.findByOrganization(organizationId)
    }

    @GetMapping("/organization/{organizationId}/with-employees")
    fun findByOrganizationWithEmployees(@PathVariable("organizationId") organizationId: Long): List<Department?>? {
        LOGGER.info("Department find: organizationId={}", organizationId)
        val departments = repository.findByOrganization(organizationId)
        departments.forEach(Consumer { d: Department? ->
            d!!.employees = employeeClient.findByDepartment(d.id)
        })
        return departments
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DepartmentController::class.java)
    }
}
