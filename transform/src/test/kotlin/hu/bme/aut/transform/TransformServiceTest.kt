package hu.bme.aut.transform

import hu.bme.aut.transform.domain.*
import hu.bme.aut.transform.repository.*
import hu.bme.aut.transform.service.*
import hu.bme.aut.transform.service.spotify.*
import hu.bme.aut.transform.service.youtube.*
import hu.bme.aut.transformapi.dto.SyncDto
import hu.bme.aut.transformapi.dto.req.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime


@ExtendWith(MockitoExtension::class)
class TransformServiceTest {

    @Mock
    private lateinit var transformRepository: TransformRepository

    @Mock
    private lateinit var connectionRepository: ConnectionRepository

    @Mock
    private lateinit var syncRepository: SyncRepository

    @Mock
    private lateinit var youtubeUserPlaylistService: YoutubeUserPlaylistService

    @Mock
    private lateinit var spotifyUserPlaylistService: SpotifyUserPlaylistService

    @Mock
    private lateinit var spotifyUrlService: SpotifyUrlService

    @Mock
    private lateinit var spotifyPlaylistTracksService: SpotifyPlaylistTracksService

    @Mock
    private lateinit var spotifyGetPlaylistService: SpotifyGetPlaylistService

    @Mock
    private lateinit var spotifySearchTrackService: SpotifySearchTrackService

    @Mock
    private lateinit var spotifyInsertTracksPlaylistService: SpotifyInsertTracksPlaylistService

    @Mock
    private lateinit var youtubeSearchService: YoutubeSearchService

    @Mock
    private lateinit var youtubeInsertItemInPlaylistService: YoutubeInsertItemInPlaylistService

    @InjectMocks
    private lateinit var transformService: TransformService


    @Test
    fun `test getTransformsByMovesongEmail`() {
        val req = GetTransformsByMovesongEmailReq("test@example.com")
        val transforms = listOf(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.SPOTIFY,
                playlistName = "Test Playlist",
                originPlaylistId = "originId",
                destinationPlaylistId = "destinationId",
                movesongEmail = "test@example.com",
                itemCount = 10,
                thumbnailUrl = "http://example.com/thumbnail.jpg",
                date = LocalDate.now()
            )
        )

        `when`(transformRepository.findAllByMovesongEmail(req.movesongEmail)).thenReturn(transforms)

        val response = transformService.getTransformsByMovesongEmail(req)

        assertEquals(1, response.transforms.size)
        assertEquals("Test Playlist", response.transforms[0].playlistName)
        assertEquals("test@example.com", response.transforms[0].movesongEmail)
    }

    @Test
    fun `test getTransformsByOriginPlaylistId`() {
        val req = GetTransformsByOriginPlaylistIdReq("originId")
        val transforms = listOf(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.SPOTIFY,
                playlistName = "Test Playlist",
                originPlaylistId = "originId",
                destinationPlaylistId = "destinationId",
                movesongEmail = "test@example.com",
                itemCount = 10,
                thumbnailUrl = "http://example.com/thumbnail.jpg",
                date = LocalDate.now()
            )
        )

        `when`(transformRepository.findAllByOriginPlaylistId(req.originPlaylistId)).thenReturn(transforms)

        val response = transformService.getTransformsByOriginPlaylistId(req)

        assertEquals(1, response.transforms.size)
        assertEquals("Test Playlist", response.transforms[0].playlistName)
        assertEquals("originId", response.transforms[0].originPlaylistId)
    }

    @Test
    fun `test getTransformsByOriginPlaylistIdAndDestinationPlaylistId`() {
        val req = GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdReq("originId", "destinationId")
        val transforms = listOf(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.SPOTIFY,
                playlistName = "Test Playlist",
                originPlaylistId = "originId",
                destinationPlaylistId = "destinationId",
                movesongEmail = "test@example.com",
                itemCount = 10,
                thumbnailUrl = "http://example.com/thumbnail.jpg",
                date = LocalDate.now()
            )
        )

        `when`(transformRepository.findAllByOriginPlaylistIdAndDestinationPlaylistId(req.originPlaylistId, req.destinationPlaylistId)).thenReturn(transforms)

        val response = transformService.getTransformsByOriginPlaylistIdAndDestinationPlaylistId(req)

        assertEquals(1, response.transforms.size)
        assertEquals("Test Playlist", response.transforms[0].playlistName)
        assertEquals("originId", response.transforms[0].originPlaylistId)
        assertEquals("destinationId", response.transforms[0].destinationPlaylistId)
    }

    @Test
    fun `test getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail`() {
        val req = GetTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmailReq("originId", "destinationId", "test@example.com")
        val transforms = listOf(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.SPOTIFY,
                playlistName = "Test Playlist",
                originPlaylistId = "originId",
                destinationPlaylistId = "destinationId",
                movesongEmail = "test@example.com",
                itemCount = 10,
                thumbnailUrl = "http://example.com/thumbnail.jpg",
                date = LocalDate.now()
            )
        )

        `when`(transformRepository.findAllByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(req.originPlaylistId, req.destinationPlaylistId, req.movesongEmail)).thenReturn(transforms)

        val response = transformService.getTransformsByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(req)

        assertEquals(1, response.transforms.size)
        assertEquals("Test Playlist", response.transforms[0].playlistName)
        assertEquals("originId", response.transforms[0].originPlaylistId)
        assertEquals("destinationId", response.transforms[0].destinationPlaylistId)
        assertEquals("test@example.com", response.transforms[0].movesongEmail)
    }

    @Test
    fun `test getTransformsByDestinationPlaylistId`() {
        val req = GetTransformsByDestinationPlaylistIdReq("destinationId")
        val transforms = listOf(
            Transform(
                originPlatform = PlatformType.YOUTUBE,
                destinationPlatform = PlatformType.SPOTIFY,
                playlistName = "Test Playlist",
                originPlaylistId = "originId",
                destinationPlaylistId = "destinationId",
                movesongEmail = "test@example.com",
                itemCount = 10,
                thumbnailUrl = "http://example.com/thumbnail.jpg",
                date = LocalDate.now()
            )
        )

        `when`(transformRepository.findAllByDestinationPlaylistId(req.destinationPlaylistId)).thenReturn(transforms)

        val response = transformService.getTransformsByDestinationPlaylistId(req)

        assertEquals(1, response.transforms.size)
        assertEquals("Test Playlist", response.transforms[0].playlistName)
        assertEquals("destinationId", response.transforms[0].destinationPlaylistId)
    }

    @Test
    fun `test createSync`() {

        val req = CreateSyncReq(
            sync = SyncDto(
                id = 1L,
                enabled = true,
                playlistName = "Test Playlist",
                youtubePlaylistId = "youtubePlaylistId",
                spotifyPlaylistId = "spotifyPlaylistId",
                movesongEmail = "asd@asd.com",
                date = LocalDateTime.now().toString(),
                lastSyncDate = LocalDateTime.now().toString(),
                interval = 24
            )
        )
        val sync = Sync()
        sync.update(req.sync)

        `when`(syncRepository.save(any(Sync::class.java))).thenReturn(sync)

        val response = transformService.createSync(req)

        assertEquals(sync.id, response.sync.id)
        assertEquals(sync.movesongEmail, response.sync.movesongEmail)
        assertEquals(sync.youtubePlaylistId, response.sync.youtubePlaylistId)
        assertEquals(sync.spotifyPlaylistId, response.sync.spotifyPlaylistId)
        assertEquals(sync.interval, response.sync.interval)
    }

    @Test
    fun `test getSyncsByMovesongEmail`() {
        val req = GetSyncsByMovesongEmailReq("test@example.com")
        val syncDto =
             SyncDto(
                id = 1L,
                enabled = true,
                playlistName = "Test Playlist",
                youtubePlaylistId = "youtubePlaylistId",
                spotifyPlaylistId = "spotifyPlaylistId",
                movesongEmail = "test@example.com",
                date = LocalDateTime.now().toString(),
                lastSyncDate = LocalDateTime.now().toString(),
                interval = 24
            )
        val sync = Sync()
        sync.update(syncDto)
        val syncs = listOf(
            sync
        )

        `when`(syncRepository.findAllByMovesongEmail(req.movesongEmail)).thenReturn(syncs)

        val response = transformService.getSyncsByMovesongEmail(req)

        assertEquals(1, response.syncs.size)
        assertEquals("test@example.com", response.syncs[0].movesongEmail)
    }

    @Test
    fun `test deleteSync`() {
        val req = DeleteSyncReq(1L)

        doNothing().`when`(syncRepository).deleteById(req.id)

        val response = transformService.deleteSync(req)

        assertEquals(true, response.success)
    }

    @Test
    fun `test deleteTransformsByMovesongEmail`() {
        val req = DeleteTransformsByMovesongEmailReq("test@example.com")

        doNothing().`when`(transformRepository).deleteAllByMovesongEmail(req.movesongEmail)

        val response = transformService.deleteTransformsByMovesongEmail(req)

        assertTrue(response.success)
        verify(transformRepository).deleteAllByMovesongEmail(req.movesongEmail)
    }

    @Test
    fun `test deleteSyncsByMovesongEmail`() {
        val req = DeleteSyncsByMovesongEmailReq("test@example.com")

        doNothing().`when`(syncRepository).deleteAllByMovesongEmail(req.movesongEmail)

        val response = transformService.deleteSyncsByMovesongEmail(req)

        assertTrue(response.success)
        verify(syncRepository).deleteAllByMovesongEmail(req.movesongEmail)
    }
}