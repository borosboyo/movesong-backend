package hu.bme.aut.transform.service.spotify

import hu.bme.aut.transform.domain.PlatformType
import hu.bme.aut.transform.repository.ConnectionRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest


@Service
open class SpotifyUrlService(
    private val connectionRepository: ConnectionRepository
) {
    @Value("\${movesong.spotify.clientId}")
    private lateinit var spotifyClientId: String

    @Value("\${movesong.spotify.clientSecret}")
    private lateinit var spotifyClientSecret: String

    @Transactional
    open fun initSpotifyApi(movesongEmail: String): SpotifyApi {
        val connection = connectionRepository.findByMovesongEmailAndPlatformType(
            movesongEmail,
            PlatformType.SPOTIFY
        )

        val spotifyApi = SpotifyApi.Builder()
            .setClientId(spotifyClientId)
            .setClientSecret(spotifyClientSecret)
            .setRefreshToken(connection.refreshToken)
            .build()

        val authorizationCodeRefreshRequest: AuthorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh()
            .build()
        val authorizationCodeCredentials: AuthorizationCodeCredentials = authorizationCodeRefreshRequest.execute()

        spotifyApi.accessToken = authorizationCodeCredentials.accessToken

        connection.accessToken = authorizationCodeCredentials.accessToken
        connectionRepository.save(connection)

        return spotifyApi
    }
}