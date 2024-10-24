package hu.bme.aut.subscription.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import hu.bme.aut.subscription.domain.SubscriptionEntity;
import hu.bme.aut.subscription.repository.CustomerStripeRepository;
import hu.bme.aut.subscription.repository.ProductStripeRepository;
import hu.bme.aut.subscription.repository.SubscriptionRepository;
import hu.bme.aut.subscriptionapi.dto.req.CancelSubscriptionReq;
import hu.bme.aut.subscriptionapi.dto.req.FindSubscriptionByUserEmailReq;
import hu.bme.aut.subscriptionapi.dto.req.FindSubscriptionReq;
import hu.bme.aut.subscriptionapi.dto.req.SaveSubscriptionReq;
import hu.bme.aut.subscriptionapi.dto.req.SubscriptionReq;
import hu.bme.aut.subscriptionapi.dto.resp.CancelSubscriptionResp;
import hu.bme.aut.subscriptionapi.dto.resp.FindSubscriptionResp;
import hu.bme.aut.subscriptionapi.dto.resp.SaveSubscriptionResp;
import hu.bme.aut.subscriptionapi.dto.resp.SubscriptionResp;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final CustomerStripeRepository customerStripeRepository;
    private final ProductStripeRepository productStripeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);

    @Value("${movesong.stripe.secretKey}")
    private String stripeSecretKey;

    @Value("${movesong.clientBaseUrl}")
    private String clientBaseUrl;

    public SubscriptionService(CustomerStripeRepository customerStripeRepository, ProductStripeRepository productStripeRepository, SubscriptionRepository subscriptionRepository) {
        this.customerStripeRepository = customerStripeRepository;
        this.productStripeRepository = productStripeRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public SubscriptionResp subscription(SubscriptionReq req) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Customer customer = CustomerStripeRepository.findOrCreateCustomer(req.getEmail(), req.getUsername());

        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customer.getId())
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setSuccessUrl(clientBaseUrl + "/movesong-frontend/premium/success")
                        .setCancelUrl(clientBaseUrl + "/movesong-frontend/premium/cancel");

        Product product = ProductStripeRepository.getProduct(req.getProductId());
        PriceData.Recurring.Interval interval = getInterval(req.getInterval());
        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                PriceData.builder()
                                        .setProductData(
                                                PriceData.ProductData.builder()
                                                        .putMetadata("app_id", product.getId())
                                                        .setName(product.getName())
                                                        .build()
                                        )
                                        .setCurrency(product.getDefaultPriceObject().getCurrency())
                                        .setUnitAmountDecimal(product.getDefaultPriceObject().getUnitAmountDecimal())
                                        .setRecurring(PriceData.Recurring.builder().setInterval(interval).build())
                                        .build()

                        ).build()
        );

        return new SubscriptionResp(Session.create(paramsBuilder.build()).getUrl(), customer.getId());
    }

    @Transactional
    public SaveSubscriptionResp saveSubscription(SaveSubscriptionReq req) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        SubscriptionListParams params = SubscriptionListParams.builder()
                .setCustomer(req.getCustomerId())
                .build();
        SubscriptionEntity subscriptionEntity = getSubscriptionEntity(req, Subscription.list(params).getData().getFirst());

        subscriptionRepository.save(subscriptionEntity);

        return new SaveSubscriptionResp(
                subscriptionEntity.getUserId(),
                subscriptionEntity.getCustomerId(),
                subscriptionEntity.getUserEmail(),
                subscriptionEntity.getUsername(),
                subscriptionEntity.getSubscriptionId(),
                subscriptionEntity.getProductId(),
                subscriptionEntity.getCurrentPeriodEnd(),
                subscriptionEntity.getPrice(),
                subscriptionEntity.getInterval()
        );
    }

    @NotNull
    private static SubscriptionEntity getSubscriptionEntity(SaveSubscriptionReq req, Subscription subscription) {

        return new SubscriptionEntity(
                0,
                req.getUserId(),
                req.getCustomerId(),
                req.getUserEmail(),
                req.getUsername(),
                subscription.getId(),
                subscription.getItems().getData().getFirst().getPrice().getProduct(),
                subscription.getCurrentPeriodEnd(),
                subscription.getItems().getData().getFirst().getPrice().getUnitAmountDecimal().doubleValue(),
                subscription.getItems().getData().getFirst().getPrice().getRecurring().getInterval()
        );
    }

    @Transactional
    public FindSubscriptionResp findSubscription(FindSubscriptionReq req){
        Stripe.apiKey = stripeSecretKey;
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findBySubscriptionId(req.getSubscriptionId());

        return new FindSubscriptionResp(
                subscriptionEntity.getUserId(),
                subscriptionEntity.getCustomerId(),
                subscriptionEntity.getUserEmail(),
                subscriptionEntity.getUsername(),
                subscriptionEntity.getSubscriptionId(),
                subscriptionEntity.getProductId(),
                subscriptionEntity.getCurrentPeriodEnd(),
                subscriptionEntity.getPrice(),
                subscriptionEntity.getInterval()
        );
    }

    @Transactional
    public FindSubscriptionResp findSubscriptionByUserEmail(FindSubscriptionByUserEmailReq req){
        Stripe.apiKey = stripeSecretKey;
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserEmail(req.getUserEmail());
        if(subscriptionEntity == null){
            return null;
        }
        return new FindSubscriptionResp(
                subscriptionEntity.getUserId(),
                subscriptionEntity.getCustomerId(),
                subscriptionEntity.getUserEmail(),
                subscriptionEntity.getUsername(),
                subscriptionEntity.getSubscriptionId(),
                subscriptionEntity.getProductId(),
                subscriptionEntity.getCurrentPeriodEnd(),
                subscriptionEntity.getPrice(),
                subscriptionEntity.getInterval()
        );
    }


    @Transactional
    public CancelSubscriptionResp cancelSubscription(CancelSubscriptionReq req) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Subscription subscription = Subscription.retrieve(req.getSubscriptionId());

        Subscription deletedSubscription = subscription.cancel();
        return new CancelSubscriptionResp(
                deletedSubscription.getStatus()
        );
    }

    private PriceData.Recurring.Interval getInterval(String interval) {
        return interval.equals("year") ? PriceData.Recurring.Interval.YEAR : PriceData.Recurring.Interval.MONTH;
    }

}