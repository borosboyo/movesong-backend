package hu.bme.aut.subscription.repository;

import com.stripe.model.Price;
import com.stripe.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductStripeRepository {
    static Map<String, Product> products;

    static {
        products = new HashMap<>();

        Product product = new Product();
        Price price = new Price();

        product.setName("Movesong Monthly Subscription");
        product.setId("prod_QuqcwXaww68NTo");
        price.setCurrency("usd");
        price.setUnitAmountDecimal(BigDecimal.valueOf(4.99));
        product.setDefaultPriceObject(price);
        products.put(product.getId(), product);

        product = new Product();
        price = new Price();

        product.setName("Movesong Yearly Subscription");
        product.setId("prod_QuqcNVtCp060Lm");
        price.setCurrency("usd");
        price.setUnitAmountDecimal(BigDecimal.valueOf(24.99));
        product.setDefaultPriceObject(price);
        products.put(product.getId(), product);

    }

    public static Product getProduct(String id) {
        return products.getOrDefault(id, null);
    }
}
