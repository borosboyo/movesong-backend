package hu.bme.aut.transform.service.youtube

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.Credential.AccessMethod
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import hu.bme.aut.transform.domain.Connection
import hu.bme.aut.transform.repository.ConnectionRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
open class YoutubeAuthorizationService(
    private val connectionRepository: ConnectionRepository,
    private val customAccessMethod: CustomAccessMethod
) {

    @Value("\${movesong.youtube.clientId}")
    private lateinit var youtubeClientId: String

    @Value("\${movesong.youtube.clientSecret}")
    private lateinit var youtubeClientSecret: String

    companion object {
        private const val TOKEN_SERVER_URL = "https://oauth2.googleapis.com/token"
        val httpTransport = NetHttpTransport()
        val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
    }

    @Transactional
    open fun getCredentialFromConnection(connection: Connection): Credential {
        val refreshTokenRequest =
            GoogleRefreshTokenRequest(
                httpTransport,
                jsonFactory,
                connection.refreshToken,
                youtubeClientId,
                youtubeClientSecret
            )
        val response = refreshTokenRequest.execute()
        connection.accessToken = response.accessToken
        connectionRepository.save(connection)
        return Credential.Builder(customAccessMethod)
            .setTransport(httpTransport)
            .setJsonFactory(jsonFactory)
            .setTokenServerEncodedUrl(TOKEN_SERVER_URL)
            .build()
            .setAccessToken(response.accessToken)
            .setRefreshToken(response.refreshToken)
    }
}

@Component
open class CustomAccessMethod : AccessMethod {
    override fun intercept(request: HttpRequest?, accessToken: String?) {
        request?.headers?.authorization = "Bearer $accessToken"
    }

    override fun getAccessTokenFromRequest(request: HttpRequest?): String? {
        return request?.headers?.authorization?.substringAfter("Bearer ")
    }
}
