package hu.bme.aut.employee.controller

import hu.bme.aut.employee.model.Employee
import hu.bme.aut.employee.repository.EmployeeRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class EmployeeController {
    @Autowired
    var repository: EmployeeRepository? = null
    @PostMapping("/")
    fun add(@RequestBody employee: Employee): Employee? {
        LOGGER.info("Employee add: {}", employee)
        return repository!!.add(employee)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): Employee? {
        LOGGER.info("Employee find: id={}", id)
        return repository!!.findById(id)
    }

    @GetMapping("/")
    fun findAll(): List<Employee?>? {
        LOGGER.info("Employee find")
        return repository!!.findAll()
    }

    @GetMapping("/department/{departmentId}")
    fun findByDepartment(@PathVariable("departmentId") departmentId: Long): List<Employee?>? {
        LOGGER.info("Employee find: departmentId={}", departmentId)
        return repository!!.findByDepartment(departmentId)
    }

    @GetMapping("/organization/{organizationId}")
    fun findByOrganization(@PathVariable("organizationId") organizationId: Long): List<Employee?>? {
        LOGGER.info("Employee find: organizationId={}", organizationId)
        return repository!!.findByOrganization(organizationId)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EmployeeController::class.java)
    }
}
