package hu.bme.aut.user.repository

import hu.bme.aut.user.domain.token.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<Token, Long> {
    fun findByToken(token: String): Token
    fun findAllByUserId(userId: Long): List<Token>
    fun deleteAllByUserId(userId: Long)
}