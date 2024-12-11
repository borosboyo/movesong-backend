package hu.bme.aut.share

import hu.bme.aut.share.domain.Share
import hu.bme.aut.share.domain.SharePlatformType
import hu.bme.aut.share.repository.ShareRepository
import hu.bme.aut.share.service.ShareService
import hu.bme.aut.shareapi.dto.req.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ShareServiceTest {

    @Mock
    private lateinit var shareRepository: ShareRepository

    @InjectMocks
    private lateinit var shareService: ShareService

    private lateinit var share: Share

    @BeforeEach
    fun setUp() {
        share = Share(
            playlistId = "testPlaylistId",
            sharedPlaylistName = "Test Playlist",
            movesongEmail = "test@example.com",
            visible = true,
            views = 0,
            sharePlatformType = SharePlatformType.YOUTUBE,
            selectedBackgroundId = 1,
            thumbnailUrl = "http://example.com/image.jpg"
        )
    }

    @Test
    fun `test createShare`() {
        val request = CreateShareReq(share.toDto())
        `when`(shareRepository.save(any(Share::class.java))).thenReturn(share)

        val response = shareService.createShare(request)

        assertEquals(request.share.playlistId, response.share.playlistId)
        assertEquals(request.share.sharedPlaylistName, response.share.sharedPlaylistName)
        verify(shareRepository, times(1)).save(any(Share::class.java))
    }

    @Test
    fun `test getSharesByMovesongEmail`() {
        val email = "test@example.com"
        val request = GetSharesByMovesongEmailReq(email)
        `when`(shareRepository.findAllBymovesongEmail(email)).thenReturn(listOf(share))

        val response = shareService.getSharesByMovesongEmail(request)

        assertEquals(1, response.shares.size)
        assertEquals(email, response.shares[0].movesongEmail)
        verify(shareRepository, times(1)).findAllBymovesongEmail(email)
    }

    @Test
    fun `test getShareById`() {
        val request = GetShareByIdReq(share.id)
        `when`(shareRepository.findById(share.id)).thenReturn(Optional.of(share))
        `when`(shareRepository.save(any(Share::class.java))).thenReturn(share) // Ensure save returns a non-null Share

        val response = shareService.getShareById(request)

        assertEquals(share.id, response.share.id)
        assertEquals(share.views, response.share.views)
        verify(shareRepository, times(1)).save(share)
    }


    @Test
    fun `test updateShare`() {
        val updatedShare = share.toDto().copy(sharedPlaylistName = "Updated Playlist Name")
        val request = UpdateShareReq(updatedShare)
        `when`(shareRepository.findById(share.id)).thenReturn(Optional.of(share))
        `when`(shareRepository.save(any(Share::class.java))).thenReturn(share)

        val response = shareService.updateShare(request)

        assertEquals(updatedShare.sharedPlaylistName, response.share.sharedPlaylistName)
        verify(shareRepository, times(1)).save(share)
    }

    @Test
    fun `test deleteShares`() {
        val request = DeleteSharesReq(share.movesongEmail)
        doNothing().`when`(shareRepository).deleteAllByMovesongEmail(share.movesongEmail)

        val response = shareService.deleteShares(request)

        verify(shareRepository, times(1)).deleteAllByMovesongEmail(share.movesongEmail)
    }
}
