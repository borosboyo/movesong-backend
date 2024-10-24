package hu.bme.aut.user.repository

import hu.bme.aut.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
    fun findByEmail(email: String): User
    fun findByUsername(username: String): User
    fun deleteUserByUsername(username: String)
    fun deleteUserByEmail(email: String)
}