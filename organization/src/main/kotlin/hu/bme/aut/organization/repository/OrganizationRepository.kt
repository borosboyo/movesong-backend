package hu.bme.aut.organization.repository

import hu.bme.aut.organization.model.Organization

class OrganizationRepository {
    private val organizations: MutableList<Organization> = ArrayList()
    fun add(organization: Organization): Organization {
        organization.id = (organizations.size + 1).toLong()
        organizations.add(organization)
        return organization
    }

    fun findById(id: Long): Organization {
        return organizations.stream()
            .filter { a: Organization -> a.id == id }
            .findFirst()
            .orElseThrow()
    }

    fun findAll(): List<Organization> {
        return organizations
    }
}
