package hu.bme.aut.discovery

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableEurekaServer
@EnableScheduling
open class DiscoveryApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DiscoveryApplication::class.java, *args)
        }
    }
}
