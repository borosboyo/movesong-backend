package hu.bme.aut.organization.client

import hu.bme.aut.organization.model.Employee
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "employee")
interface EmployeeClient {
    @GetMapping("/organization/{organizationId}")
    fun findByOrganization(@PathVariable("organizationId") organizationId: Long?): List<Employee?>?
}
