package hu.bme.aut.transform.repository

import hu.bme.aut.transform.domain.Transform
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransformRepository: JpaRepository<Transform, Long> {
    fun findAllByOriginPlaylistId(originPlaylistId: String): List<Transform>
    fun findAllByDestinationPlaylistId(destinationPlaylistId: String): List<Transform>
    fun findAllByOriginPlaylistIdAndDestinationPlaylistId(originPlaylistId: String, destinationPlaylistId: String): List<Transform>
    fun findAllByOriginPlaylistIdAndDestinationPlaylistIdAndMovesongEmail(originPlaylistId: String, destinationPlaylistId: String, movesongEmail: String): List<Transform>
    fun findAllByMovesongEmail(movesongEmail: String): List<Transform>
}