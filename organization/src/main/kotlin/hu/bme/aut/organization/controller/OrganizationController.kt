package hu.bme.aut.organization.controller

import hu.bme.aut.organization.client.DepartmentClient
import hu.bme.aut.organization.client.EmployeeClient
import hu.bme.aut.organization.model.Organization
import hu.bme.aut.organization.repository.OrganizationRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class OrganizationController {
    @Autowired
    var repository: OrganizationRepository? = null

    @Autowired
    var departmentClient: DepartmentClient? = null

    @Autowired
    var employeeClient: EmployeeClient? = null
    @PostMapping("/")
    fun add(@RequestBody organization: Organization?): Organization {
        LOGGER.info("Organization add: {}", organization)
        return repository!!.add(organization!!)
    }

    @GetMapping("/")
    fun findAll(): List<Organization> {
        LOGGER.info("Organization find")
        return repository!!.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long?): Organization {
        LOGGER.info("Organization find: id={}", id)
        return repository!!.findById(id!!)
    }

    @GetMapping("/{id}/with-departments")
    fun findByIdWithDepartments(@PathVariable("id") id: Long?): Organization {
        LOGGER.info("Organization find: id={}", id)
        val organization = repository!!.findById(id!!)
        organization.departments = departmentClient!!.findByOrganization(organization.id)
        return organization
    }

    @GetMapping("/{id}/with-departments-and-employees")
    fun findByIdWithDepartmentsAndEmployees(@PathVariable("id") id: Long?): Organization {
        LOGGER.info("Organization find: id={}", id)
        val organization = repository!!.findById(id!!)
        organization.departments = departmentClient!!.findByOrganizationWithEmployees(organization.id)
        return organization
    }

    @GetMapping("/{id}/with-employees")
    fun findByIdWithEmployees(@PathVariable("id") id: Long?): Organization {
        LOGGER.info("Organization find: id={}", id)
        val organization = repository!!.findById(id!!)
        organization.employees = employeeClient!!.findByOrganization(organization.id)
        return organization
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(OrganizationController::class.java)
    }
}
