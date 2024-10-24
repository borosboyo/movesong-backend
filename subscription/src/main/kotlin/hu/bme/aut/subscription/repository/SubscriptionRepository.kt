package hu.bme.aut.subscription.repository

import hu.bme.aut.subscription.domain.SubscriptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository : JpaRepository<SubscriptionEntity, Long> {
    fun findByUserId(userId: Long): List<SubscriptionEntity>
    fun deleteByUserId(userId: Long)
    fun findBySubscriptionId(subscriptionId: String): SubscriptionEntity
    fun findByCustomerId(customerId: String): SubscriptionEntity
    fun findByUserEmail(userEmail: String): SubscriptionEntity?
}