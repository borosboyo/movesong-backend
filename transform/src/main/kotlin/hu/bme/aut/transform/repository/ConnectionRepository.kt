package hu.bme.aut.transform.repository

import hu.bme.aut.transform.domain.Connection
import hu.bme.aut.transform.domain.ConnectionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConnectionRepository: JpaRepository<Connection, Long> {
    fun findAllByMovesongEmail(movesongEmail: String): List<Connection>
    fun findByMovesongEmailAndConnectionType(movesongEmail: String, connectionType: ConnectionType): Connection
}