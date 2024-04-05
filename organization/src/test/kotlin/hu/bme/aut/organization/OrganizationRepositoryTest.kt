package hu.bme.aut.organization

import hu.bme.aut.organization.model.Organization
import hu.bme.aut.organization.repository.OrganizationRepository
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.util.Assert

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrganizationRepositoryTest{
    @Test
    @Order(1)
    fun testAddOrganization() {
        var organization = Organization("Test", "Test Street")
        organization = repository.add(organization)
        Assert.notNull(organization, "Organization is null.")
        Assert.isTrue(organization.id == 1L, "Organization bad id.")
    }

    @Test
    @Order(2)
    fun testFindAll() {
        val organizations = repository.findAll()
        Assert.isTrue(organizations.size == 1, "Organizations size is wrong.")
        Assert.isTrue(organizations[0].id == 1L, "Organization bad id.")
    }

    @Test
    @Order(3)
    fun testFindById() {
        val organization = repository.findById(1L)
        Assert.notNull(organization, "Organization not found.")
        Assert.isTrue(organization.id == 1L, "Organization bad id.")
    }
    companion object {
        private val repository = OrganizationRepository()
    }
}
