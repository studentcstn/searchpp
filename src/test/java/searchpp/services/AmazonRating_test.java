package searchpp.services;

import org.junit.Test;
import searchpp.model.products.AmazonProduct;

public class AmazonRating_test {

    @Test
    public void getRating() {
        AmazonProduct product = new AmazonProduct();
        product.setProductId("B01CD5VC92");
        AmazonRating.getRating(product);
    }
}
