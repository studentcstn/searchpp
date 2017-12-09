package searchpp.services;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductSearcher_test {

    @Test
    public void searchAmazonProduct() {
        ProductSearcher.searchAmazonProduct("Raspberry pi");
    }

    @Test
    public void searchEbayProduct() {
        ProductSearcher.searchEbayProduct("Raspberry pi");
    }
}
