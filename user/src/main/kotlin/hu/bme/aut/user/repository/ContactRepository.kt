package hu.bme.aut.user.repository

import hu.bme.aut.user.domain.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {
}