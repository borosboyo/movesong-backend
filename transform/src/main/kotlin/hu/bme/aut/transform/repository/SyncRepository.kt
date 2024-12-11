package hu.bme.aut.transform.repository

import hu.bme.aut.transform.domain.Sync
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SyncRepository: JpaRepository<Sync, Long> {
    fun findAllByMovesongEmail(movesongEmail: String): List<Sync>
    fun findAllByEnabled(enabled: Boolean): List<Sync>
    fun deleteAllByMovesongEmail(movesongEmail: String)
}