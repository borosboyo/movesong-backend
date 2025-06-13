package hu.bme.aut.transform.service.spotify

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.io.IOException
import org.apache.hc.core5.http.ParseException
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@Service
open class SpotifyUserProfileService {

    @Transactional
    open fun getCurrentUserProfile(spotifyApi: SpotifyApi): User? {
        val getCurrentUsersProfileRequest: GetCurrentUsersProfileRequest =
            spotifyApi.currentUsersProfile.build()

        try {
            val currentUser: User = getCurrentUsersProfileRequest.execute()

            return currentUser
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        } catch (e: SpotifyWebApiException) {
            throw RuntimeException(e)
        }
    }
}