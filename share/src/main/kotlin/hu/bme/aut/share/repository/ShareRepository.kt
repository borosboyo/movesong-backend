package hu.bme.aut.share.repository

import hu.bme.aut.share.domain.Share
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShareRepository: JpaRepository<Share, Long>{
    fun findAllBymovesongEmail(movesongEmail: String): List<Share>
    fun deleteAllByMovesongEmail(movesongEmail: String)
}