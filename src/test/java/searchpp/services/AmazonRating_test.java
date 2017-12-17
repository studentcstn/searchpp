package searchpp.services;

import org.junit.Test;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.AmazonProductRating;

public class AmazonRating_test {

    @Test
    public void getRating() {
        AmazonProduct product = new AmazonProduct();
        product.setProductId("B01CD5VC92");
        AmazonProductRating amazonProductRating = AmazonRating.getRating(product);
        System.out.println(amazonProductRating);

        product.setProductId("B01MQ22LA9");
        amazonProductRating = AmazonRating.getRating(product);
        System.out.println(amazonProductRating);

        product.setProductId("B01CI58722");
        amazonProductRating = AmazonRating.getRating(product);
        System.out.println(amazonProductRating);
    }
}
