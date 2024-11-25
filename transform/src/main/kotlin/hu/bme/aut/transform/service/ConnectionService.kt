package hu.bme.aut.transform.service

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import hu.bme.aut.transform.domain.Connection
import hu.bme.aut.transform.domain.PlatformType
import hu.bme.aut.transform.repository.ConnectionRepository
import hu.bme.aut.transformapi.dto.ConnectionDto
import hu.bme.aut.transformapi.dto.req.ConnectSpotifyAccountReq
import hu.bme.aut.transformapi.dto.req.ConnectYoutubeAccountReq
import hu.bme.aut.transformapi.dto.req.DeleteConnectionsByMovesongEmailReq
import hu.bme.aut.transformapi.dto.req.FindConnectionsByMovesongEmailReq
import hu.bme.aut.transformapi.dto.resp.ConnectSpotifyAccountResp
import hu.bme.aut.transformapi.dto.resp.ConnectYoutubeAccountResp
import hu.bme.aut.transformapi.dto.resp.DeleteConnectionsByMovesongEmailResp
import hu.bme.aut.transformapi.dto.resp.FindConnectionsByMovesongEmailResp
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.SpotifyHttpManager
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest


@Service
open class ConnectionService(
    private val connectionRepository: ConnectionRepository,
) {
    //Youtube
    @Value("\${movesong.youtube.tokenUri}")
    lateinit var youtubeTokenUri: String

    @Value("\${movesong.youtube.clientId}")
    lateinit var youtubeClientId: String

    @Value("\${movesong.youtube.clientSecret}")
    lateinit var youtubeClientSecret: String

    @Value("\${movesong.youtube.redirectUri}")
    lateinit var youtubeRedirectUri: String

    //Spotify
    @Value("\${movesong.spotify.tokenUri}")
    lateinit var spotifyTokenUri: String

    @Value("\${movesong.spotify.clientId}")
    lateinit var spotifyClientId: String

    @Value("\${movesong.spotify.clientSecret}")
    lateinit var spotifyClientSecret: String

    @Value("\${movesong.spotify.redirectUri}")
    lateinit var spotifyRedirectUri: String

    @Transactional
    open fun connectYoutubeAccount(req: ConnectYoutubeAccountReq): ConnectYoutubeAccountResp {
        println("Youtube code: " + req.code)
        println(youtubeClientId)
        println(youtubeClientSecret)
        println(youtubeTokenUri)
        println(youtubeRedirectUri)
        val tokenRequest = GoogleAuthorizationCodeTokenRequest(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            youtubeTokenUri,
            youtubeClientId,
            youtubeClientSecret,
            req.code,
            youtubeRedirectUri
        )

        val response: GoogleTokenResponse = tokenRequest.execute()
        connectionRepository.save(
            Connection(
                movesongEmail = req.movesongEmail,
                platformType = PlatformType.YOUTUBE,
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        )
        return ConnectYoutubeAccountResp(true)
    }

    @Transactional
    open fun connectSpotifyAccount(req: ConnectSpotifyAccountReq): ConnectSpotifyAccountResp {
        LOGGER.info("Spotify code: " + req.code)
        val spotifyApi: SpotifyApi = SpotifyApi.Builder()
            .setClientId(spotifyClientId)
            .setClientSecret(spotifyClientSecret)
            .setRedirectUri(SpotifyHttpManager.makeUri(spotifyRedirectUri))
            .build()

        val authorizationCodeRequest: AuthorizationCodeRequest = spotifyApi.authorizationCode(req.code).build()
        val authorizationCodeCredentials: AuthorizationCodeCredentials = authorizationCodeRequest.execute()
        spotifyApi.accessToken = authorizationCodeCredentials.accessToken
        spotifyApi.refreshToken = authorizationCodeCredentials.refreshToken

        LOGGER.info("Expires in: " + authorizationCodeCredentials.expiresIn)
        LOGGER.info("Access Token: " + authorizationCodeCredentials.accessToken)
        LOGGER.info("Refresh Token: " + authorizationCodeCredentials.refreshToken)
        connectionRepository.save(
            Connection(
                movesongEmail = req.movesongEmail,
                platformType = PlatformType.SPOTIFY,
                accessToken = authorizationCodeCredentials.accessToken,
                refreshToken = authorizationCodeCredentials.refreshToken
            )
        )
        return ConnectSpotifyAccountResp(true)
    }

    @Transactional
    open fun findConnectionsByMovesongEmail(req: FindConnectionsByMovesongEmailReq): FindConnectionsByMovesongEmailResp {
        val connections = connectionRepository.findAllByMovesongEmail(req.movesongEmail)
        val connectionsDto = connections.map { connection -> ConnectionDto(connection.movesongEmail, connection.platformType.toString(), connection.accessToken, connection.refreshToken) }
        return FindConnectionsByMovesongEmailResp(connectionsDto)
    }

    @Transactional
    open fun deleteConnectionsByMovesongEmail(req: DeleteConnectionsByMovesongEmailReq): DeleteConnectionsByMovesongEmailResp {
        connectionRepository.deleteAllByMovesongEmail(req.movesongEmail)
        return DeleteConnectionsByMovesongEmailResp(
            success = true
        )
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ConnectionService::class.java)
    }
}