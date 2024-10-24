package hu.bme.aut.organization.client

import hu.bme.aut.organization.model.Department
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "department")
interface DepartmentClient {
    @GetMapping("/organization/{organizationId}")
    fun findByOrganization(@PathVariable("organizationId") organizationId: Long?): List<Department?>?

    @GetMapping("/organization/{organizationId}/with-employees")
    fun findByOrganizationWithEmployees(@PathVariable("organizationId") organizationId: Long?): List<Department?>?
}
