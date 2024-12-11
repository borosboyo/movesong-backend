package hu.bme.aut.gateway

import hu.bme.aut.authhelper.CORSFilter
import hu.bme.aut.authhelper.WebSecurityConfig
import org.slf4j.LoggerFactory
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.gateway.route.RouteDefinition
import org.springframework.cloud.gateway.route.RouteDefinitionLocator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@Import(
    WebSecurityConfig::class, CORSFilter::class
)
open class GatewayApplication {
    @Autowired
    var locator: RouteDefinitionLocator? = null

    @Bean
    open fun apis(): List<GroupedOpenApi> {
        val groups: MutableList<GroupedOpenApi> = ArrayList()
        val definitions = locator!!.routeDefinitions.collectList().block()!!
        definitions.stream().filter { routeDefinition: RouteDefinition -> routeDefinition.id.matches(".*".toRegex()) }
            .forEach { routeDefinition: RouteDefinition ->
                val name = routeDefinition.id.replace("".toRegex(), "")
                groups.add(GroupedOpenApi.builder().pathsToMatch("/$name/**").group(name).build())
            }
        LOGGER.info("APIs: $groups")
        return groups
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GatewayApplication::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(GatewayApplication::class.java, *args)
        }
    }
}
