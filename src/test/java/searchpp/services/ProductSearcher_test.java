package searchpp.services;

import org.junit.Test;
import searchpp.utils.ConfigLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ProductSearcher_test {

    private void loadTestConfig() {
        ConfigLoader configLoader = ConfigLoader.getInstance();

        File testConfig = new File("src/test/java/searchpp/services/searchpp.conf");
        if (!testConfig.exists())
            return;

        //change file and load new
        try {
            Class c = configLoader.getClass();
            Field field = c.getDeclaredField("file");
            field.setAccessible(true);
            field.set(configLoader, testConfig);

            Method method = c.getDeclaredMethod("loadConfig");
            method.setAccessible(true);
            method.invoke(configLoader);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void searchAmazonProduct() {
        loadTestConfig();
        ProductSearcher.searchAmazonProduct("B01CD5VC92");
    }

    @Test
    public void searchAmazonProductList() {
        loadTestConfig();
        ProductSearcher.searchAmazonProductList("Raspberry pi");
    }

    @Test
    public void searchEbayProduct() {
        loadTestConfig();
        ProductSearcher.searchEbayProduct("122129721207");
    }

    @Test
    public void searchEbayProductList() {
        loadTestConfig();
        ProductSearcher.searchEbayProductList("Raspberry pi");
    }
}
