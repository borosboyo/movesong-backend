package hu.bme.aut.employee.repository

import hu.bme.aut.employee.model.Employee

class EmployeeRepository {
    private val employees: MutableList<Employee> = ArrayList()
    fun add(employee: Employee): Employee {
        employee.id = (employees.size + 1).toLong()
        employees.add(employee)
        return employee
    }

    fun findById(id: Long): Employee {
        return employees.stream()
            .filter { a: Employee -> a.id == id }
            .findFirst()
            .orElseThrow()
    }

    fun findAll(): List<Employee> {
        return employees
    }

    fun findByDepartment(departmentId: Long): List<Employee> {
        return employees.stream()
            .filter { a: Employee -> a.departmentId == departmentId }
            .toList()
    }

    fun findByOrganization(organizationId: Long): List<Employee> {
        return employees.stream()
            .filter { a: Employee -> a.organizationId == organizationId }
            .toList()
    }
}
