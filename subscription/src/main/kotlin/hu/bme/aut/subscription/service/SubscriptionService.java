package hu.bme.aut.subscription.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import hu.bme.aut.subscription.domain.CustomerStripeRepository;
import hu.bme.aut.subscription.domain.ProductStripeRepository;
import hu.bme.aut.subscriptionapi.dto.req.CancelSubscriptionReq;
import hu.bme.aut.subscriptionapi.dto.req.FindSubscriptionReq;
import hu.bme.aut.subscriptionapi.dto.req.SubscriptionReq;
import hu.bme.aut.subscriptionapi.dto.resp.CancelSubscriptionResp;
import hu.bme.aut.subscriptionapi.dto.resp.FindSubscriptionResp;
import hu.bme.aut.subscriptionapi.dto.resp.SubscriptionResp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final CustomerStripeRepository customerStripeRepository;
    private final ProductStripeRepository productStripeRepository;

    @Value("${movesong.stripe.secretKey}")
    private String stripeSecretKey;

    @Value("${movesong.clientBaseUrl}")
    private String clientBaseUrl;

    public SubscriptionService(CustomerStripeRepository customerStripeRepository, ProductStripeRepository productStripeRepository) {
        this.customerStripeRepository = customerStripeRepository;
        this.productStripeRepository = productStripeRepository;
    }

    @Transactional
    public SubscriptionResp subscription(SubscriptionReq req) throws StripeException {
        Customer customer = CustomerStripeRepository.findOrCreateCustomer(req.getEmail(), req.getUsername());

        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customer.getId())
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setSuccessUrl(clientBaseUrl + "/success")
                        .setCancelUrl(clientBaseUrl + "/cancel");

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

        return new SubscriptionResp(Session.create(paramsBuilder.build()).getUrl());
    }

    @Transactional
    public FindSubscriptionResp findSubscription(FindSubscriptionReq req) throws StripeException {
        Subscription subscription = Subscription.retrieve(req.getSubscriptionId());

        return new FindSubscriptionResp("");
    }

    @Transactional
    public CancelSubscriptionResp cancelSubscription(CancelSubscriptionReq req) throws StripeException {
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