package hu.bme.aut.transform.repository

import hu.bme.aut.transform.domain.Transform
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransformRepository: JpaRepository<Transform, Long> {
}