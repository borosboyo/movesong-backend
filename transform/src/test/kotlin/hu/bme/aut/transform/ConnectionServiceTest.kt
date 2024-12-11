package hu.bme.aut.transform

import hu.bme.aut.transform.domain.Connection
import hu.bme.aut.transform.domain.PlatformType
import hu.bme.aut.transform.repository.ConnectionRepository
import hu.bme.aut.transform.service.ConnectionService
import hu.bme.aut.transformapi.dto.req.DeleteConnectionsByMovesongEmailReq
import hu.bme.aut.transformapi.dto.req.FindConnectionsByMovesongEmailReq
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ConnectionServiceTest {

    @Mock
    private lateinit var connectionRepository: ConnectionRepository

    @InjectMocks
    private lateinit var connectionService: ConnectionService

    @Test
    fun `findConnectionsByMovesongEmail should return connections`() {
        val req = FindConnectionsByMovesongEmailReq("test@example.com")
        val connections = listOf(
            Connection(1L, "test@example.com", PlatformType.YOUTUBE, "accessToken1", "refreshToken1"),
            Connection(2L, "test@example.com", PlatformType.SPOTIFY, "accessToken2", "refreshToken2")
        )

        `when`(connectionRepository.findAllByMovesongEmail(req.movesongEmail)).thenReturn(connections)

        val response = connectionService.findConnectionsByMovesongEmail(req)

        assertEquals(2, response.connections?.size)
        assertEquals("test@example.com", response.connections?.get(0)!!.movesongEmail)
        assertEquals(PlatformType.YOUTUBE.toString(), response.connections?.get(0)!!.platformType)
        assertEquals("accessToken1", response.connections?.get(0)!!.accessToken)
        assertEquals("refreshToken1", response.connections?.get(0)!!.refreshToken)
    }

    @Test
    fun `test deleteConnectionsByMovesongEmail`() {
        val req = DeleteConnectionsByMovesongEmailReq("test@example.com")

        doNothing().`when`(connectionRepository).deleteAllByMovesongEmail(req.movesongEmail)

        val response = connectionService.deleteConnectionsByMovesongEmail(req)

        assertTrue(response.success)
        verify(connectionRepository).deleteAllByMovesongEmail(req.movesongEmail)
    }
}