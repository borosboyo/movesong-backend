package hu.bme.aut.department.repository

import hu.bme.aut.department.model.Department

class DepartmentRepository {
    private val departments: MutableList<Department> = ArrayList()
    fun add(department: Department): Department {
        department.id = (departments.size + 1).toLong()
        departments.add(department)
        return department
    }

    fun findById(id: Long): Department {
        return departments.stream()
            .filter { a: Department -> a.id == id }
            .findFirst()
            .orElseThrow()
    }

    fun findAll(): List<Department> {
        return departments
    }

    fun findByOrganization(organizationId: Long): List<Department> {
        return departments.stream()
            .filter { a: Department -> a.organizationId == organizationId }
            .toList()
    }
}
