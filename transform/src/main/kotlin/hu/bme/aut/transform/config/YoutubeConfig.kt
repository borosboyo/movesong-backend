package hu.bme.aut.transform.config

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class YouTubeConfig {

    private val logger = LoggerFactory.getLogger(YouTubeConfig::class.java)

    @Bean
    @Throws(Exception::class)
    open fun youtube(): YouTube {
        try {
            val transport = GoogleNetHttpTransport.newTrustedTransport()
            val jsonFactory = GsonFactory.getDefaultInstance()
            return YouTube.Builder(transport, jsonFactory, null)
                .setApplicationName("Movesong")
                .build()
        } catch (e: Exception) {
            logger.error("Error creating YouTube bean", e)
            throw e
        }
    }
}