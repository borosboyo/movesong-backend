package hu.bme.aut.transformapi.dto.resp

import hu.bme.aut.transformapi.dto.PlaylistItemDto

data class GetItemsInYoutubePlaylistResp(
    val items: List<PlaylistItemDto>
)
