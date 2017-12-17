package searchpp.services;

import org.junit.Test;
import searchpp.databse.DbProductTest;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.EbayProduct;
import searchpp.utils.ConfigLoader;

import java.io.File;

public class ProductSearcher_test {

    private void loadTestConfig() {
        ConfigLoader configLoader = ConfigLoader.getInstance();

        File testConfig = new File("src/test/java/searchpp/services/searchpp.conf");
        if (!testConfig.exists())
            return;

        //change file and load new
        DbProductTest.loadTestConfig(configLoader, testConfig);
    }

    @Test
    public void searchAmazonProduct() {
        loadTestConfig();
        AmazonProduct testProduct = new AmazonProduct();
        testProduct.setProductId("B01CD5VC92");
        ProductSearcher.searchAmazonProduct(testProduct);
    }

    @Test
    public void searchAmazonProductList() {
        loadTestConfig();
        ProductSearcher.searchAmazonProductList("Raspberry pi");
    }

    @Test
    public void searchEbayProduct() {
        loadTestConfig();
        EbayProduct testProduct = new EbayProduct();
        testProduct.setProductId("122129721207");
        ProductSearcher.searchEbayProduct(testProduct);
    }

    @Test
    public void searchEbayProductList() {
        loadTestConfig();
        ProductSearcher.searchEbayProductList("Raspberry pi");
    }
}
