package hu.bme.aut.department.client

import hu.bme.aut.department.model.Employee
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "employee")
interface EmployeeClient {
    @GetMapping("/department/{departmentId}")
    fun findByDepartment(@PathVariable("departmentId") departmentId: Long?): List<Employee?>?
}
