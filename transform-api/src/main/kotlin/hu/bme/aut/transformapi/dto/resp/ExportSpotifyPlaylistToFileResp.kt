package hu.bme.aut.transformapi.dto.resp

data class ExportSpotifyPlaylistToFileResp(
    val file: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExportSpotifyPlaylistToFileResp

        return file.contentEquals(other.file)
    }

    override fun hashCode(): Int {
        return file.contentHashCode()
    }
}
