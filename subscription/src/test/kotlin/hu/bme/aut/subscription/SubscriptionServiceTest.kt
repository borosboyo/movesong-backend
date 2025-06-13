package hu.bme.aut.subscription

import com.stripe.Stripe
import com.stripe.model.*
import com.stripe.model.checkout.Session
import com.stripe.param.SubscriptionListParams
import com.stripe.param.checkout.SessionCreateParams
import hu.bme.aut.subscription.domain.SubscriptionEntity
import hu.bme.aut.subscription.repository.CustomerStripeRepository
import hu.bme.aut.subscription.repository.ProductStripeRepository
import hu.bme.aut.subscription.repository.SubscriptionRepository
import hu.bme.aut.subscription.service.SubscriptionService
import hu.bme.aut.subscriptionapi.dto.req.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class SubscriptionServiceTest {

    @Mock
    private lateinit var customerStripeRepository: CustomerStripeRepository

    @Mock
    private lateinit var productStripeRepository: ProductStripeRepository

    @Mock
    private lateinit var subscriptionRepository: SubscriptionRepository

    @InjectMocks
    private lateinit var subscriptionService: SubscriptionService

    private lateinit var subscriptionEntity: SubscriptionEntity

    private val stripeSecretKey: String = "sk_test_51Q2wPVIRpKoZz4PJb3lMrQ1JHxSqIEnuMRU7QEFPxuVpMvR2p4Z6kpUiHZ6CTPZJeAtCzlH0jb80ztwnfgBBPRCN00JC9CR2iW"

    @BeforeEach
    fun setUp() {
        Stripe.apiKey = stripeSecretKey
        subscriptionEntity = SubscriptionEntity(
            id = 0,
            userId = 1,
            customerId = "cus_R5MpEIlqElZvZN",
            userEmail = "test@example.com",
            username = "testuser",
            subscriptionId = "sub_1QDQvrIRpKoZz4PJQoaP0VT9",
            productId = "prod_QuqcNVtCp060Lm",
            currentPeriodEnd = 1234567890,
            price = 9.99,
            interval = "month"
        )
    }

    @Test
    fun `test findSubscription`() {
        val req = FindSubscriptionReq("sub_1QDQvrIRpKoZz4PJQoaP0VT9")

        `when`(subscriptionRepository.findBySubscriptionId(req.subscriptionId)).thenReturn(subscriptionEntity)

        val response = subscriptionService.findSubscription(req)

        assertEquals(subscriptionEntity.userId, response.userId)
        assertEquals(subscriptionEntity.customerId, response.customerId)
        assertEquals(subscriptionEntity.userEmail, response.userEmail)
        assertEquals(subscriptionEntity.username, response.username)
        assertEquals(subscriptionEntity.subscriptionId, response.subscriptionId)
        assertEquals(subscriptionEntity.productId, response.productId)
        assertEquals(subscriptionEntity.currentPeriodEnd, response.currentPeriodEnd)
        assertEquals(subscriptionEntity.price, response.price)
        assertEquals(subscriptionEntity.interval, response.interval)
    }

    @Test
    fun `test findSubscriptionByUserEmail`() {
        val req = FindSubscriptionByUserEmailReq("test@example.com")

        `when`(subscriptionRepository.findByUserEmail(req.userEmail)).thenReturn(subscriptionEntity)

        val response = subscriptionService.findSubscriptionByUserEmail(req)

        assertEquals(subscriptionEntity.userId, response.userId)
        assertEquals(subscriptionEntity.customerId, response.customerId)
        assertEquals(subscriptionEntity.userEmail, response.userEmail)
        assertEquals(subscriptionEntity.username, response.username)
        assertEquals(subscriptionEntity.subscriptionId, response.subscriptionId)
        assertEquals(subscriptionEntity.productId, response.productId)
        assertEquals(subscriptionEntity.currentPeriodEnd, response.currentPeriodEnd)
        assertEquals(subscriptionEntity.price, response.price)
        assertEquals(subscriptionEntity.interval, response.interval)
    }

    @Test
    fun `test subscription`() {
        val req = SubscriptionReq("test@example.com", "testuser", "prod_QuqcwXaww68NTo", "month")
        val customer = mock(Customer::class.java)
        val product = mock(Product::class.java)
        val session = mock(Session::class.java)
        val price = Price()
        price.currency = "usd"
        price.unitAmountDecimal = BigDecimal.valueOf(4.99)


        `when`(customer.id).thenReturn("cust_123")
        `when`(product.defaultPriceObject).thenReturn(price)
        `when`(product.id).thenReturn("prod_QuqcwXaww68NTo")
        `when`(session.url).thenReturn("http://example.com/session")

        // Mock static methods for CustomerStripeRepository, ProductStripeRepository, and Session
        mockStatic(CustomerStripeRepository::class.java).use { mockedCustomerRepo ->
            mockStatic(ProductStripeRepository::class.java).use { mockedProductRepo ->
                mockStatic(Session::class.java).use { mockedSession ->
                    mockedCustomerRepo.`when`<Customer> { CustomerStripeRepository.findOrCreateCustomer(req.email, req.username) }
                        .thenReturn(customer)
                    mockedProductRepo.`when`<Product> { ProductStripeRepository.getProduct(req.productId) }
                        .thenReturn(product)
                    mockedSession.`when`<Session> { Session.create(any(SessionCreateParams::class.java)) }
                        .thenReturn(session)

                    val response = subscriptionService.subscription(req)

                    assertEquals("http://example.com/session", response.url)
                    assertEquals("cust_123", response.customerId)
                }
            }
        }
    }

    @Test
    fun `test saveSubscription`() {
        val req = SaveSubscriptionReq(1, "test@example.com", "testuser", "cust_123")
        val subscription = mock(Subscription::class.java)
        val subscriptionItemCollection = mock(SubscriptionItemCollection::class.java)
        val subscriptionItem = mock(SubscriptionItem::class.java)
        val price = mock(Price::class.java)
        val recurring = mock(Price.Recurring::class.java)
        val subscriptionCollection = mock(SubscriptionCollection::class.java)

        `when`(subscription.id).thenReturn("sub_123")
        `when`(subscription.items).thenReturn(subscriptionItemCollection)
        `when`(subscriptionItemCollection.data).thenReturn(listOf(subscriptionItem))
        `when`(subscriptionItem.price).thenReturn(price)
        `when`(price.product).thenReturn("prod_123")
        `when`(price.unitAmountDecimal).thenReturn(BigDecimal.valueOf(999))
        `when`(price.recurring).thenReturn(recurring)
        `when`(recurring.interval).thenReturn("month")
        `when`(subscriptionCollection.data).thenReturn(listOf(subscription))

        // Mock the static Subscription.list() method
        mockStatic(Subscription::class.java).use { mockedSubscription ->
            mockedSubscription.`when`<SubscriptionCollection> { Subscription.list(any(SubscriptionListParams::class.java)) }
                .thenReturn(subscriptionCollection)

            val response = subscriptionService.saveSubscription(req)

            assertEquals(req.userId, response.userId)
            assertEquals(req.customerId, response.customerId)
            assertEquals(req.userEmail, response.userEmail)
            assertEquals(req.username, response.username)
            assertEquals("sub_123", response.subscriptionId)
            assertEquals("prod_123", response.productId)
            assertEquals(999.0, response.price)
            assertEquals("month", response.interval)
        }
    }

    @Test
    fun `test cancelSubscription`() {
        val req = CancelSubscriptionReq("sub_1QDQvrIRpKoZz4PJQoaP0VT9")
        val subscription = mock(Subscription::class.java)
        val deletedSubscription = mock(Subscription::class.java)
        val subscriptionEntity = SubscriptionEntity(
            id = 0,
            userId = 1,
            customerId = "cus_R5MpEIlqElZvZN",
            userEmail = "test@example.com",
            username = "testuser",
            subscriptionId = "sub_1QDQvrIRpKoZz4PJQoaP0VT9",
            productId = "prod_QuqcNVtCp060Lm",
            currentPeriodEnd = 1234567890,
            price = 9.99,
            interval = "month"
        )

        `when`(subscriptionRepository.findBySubscriptionId(req.subscriptionId)).thenReturn(subscriptionEntity)
        `when`(subscription.cancel()).thenReturn(deletedSubscription)
        `when`(deletedSubscription.status).thenReturn("canceled")

        // Mock static method for Subscription.retrieve
        mockStatic(Subscription::class.java).use { mockedSubscription ->
            mockedSubscription.`when`<Subscription> { Subscription.retrieve(req.subscriptionId) }
                .thenReturn(subscription)

            doNothing().`when`(subscriptionRepository).deleteByUserId(subscriptionEntity.userId)

            val response = subscriptionService.cancelSubscription(req)

            assertEquals("canceled", response.subscriptionStatus)
            verify(subscriptionRepository).deleteByUserId(subscriptionEntity.userId)
        }
    }
}