package hu.bme.aut.organization

import hu.bme.aut.organization.model.Organization
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.cloud.discovery.enabled=false", "spring.cloud.config.discovery.enabled=false"]
)
class OrganizationControllerTests {
    @Autowired
    var restTemplate: TestRestTemplate? = null
    @Test
    fun findAll() {
        val o = restTemplate!!.getForObject("/", Array<Organization>::class.java)
        Assertions.assertTrue(o.size > 0)
    }

    @Test
    fun findById() {
        val o = restTemplate!!.getForObject("/{id}", Organization::class.java, 1)
        Assertions.assertNotNull(o)
        Assertions.assertNotNull(o.id)
        Assertions.assertEquals(1, o.id)
    }

    @Test
    fun add() {
        var o = Organization("Test", "Test1")
        o = restTemplate!!.postForObject("/", o, Organization::class.java)
        Assertions.assertNotNull(o)
        Assertions.assertNotNull(o.id)
        Assertions.assertEquals(3, o.id)
    }
}
