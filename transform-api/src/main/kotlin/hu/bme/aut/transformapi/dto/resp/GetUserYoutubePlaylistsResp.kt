package hu.bme.aut.transformapi.dto.resp

import hu.bme.aut.transformapi.dto.PlaylistDto

data class GetUserYoutubePlaylistsResp(
    val playlists: List<PlaylistDto>
)