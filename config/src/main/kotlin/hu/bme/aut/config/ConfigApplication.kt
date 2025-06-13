package hu.bme.aut.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigServer
@EnableScheduling
open class ConfigApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ConfigApplication::class.java, *args)
        }
    }
}
