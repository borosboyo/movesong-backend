package hu.bme.aut.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.cloud.discovery.enabled=false", "spring.cloud.config.discovery.enabled=false"]
)
class UserAppTests {
    @Autowired
    var restTemplate: TestRestTemplate? = null
}
