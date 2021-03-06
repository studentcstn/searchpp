package searchpp.databse;

import org.junit.Test;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.EbayProduct;
import searchpp.services.ProductSearcher;
import searchpp.utils.ConfigLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class DbProductTest
{
    public static void loadConfig()
    {
        ConfigLoader configLoader = ConfigLoader.getInstance();

        File testConfig = new File("src/test/java/searchpp/services/searchpp.conf");
        if (!testConfig.exists())
            return;

        //change file and load new
        loadTestConfig(configLoader, testConfig);
    }

    public static void loadTestConfig(ConfigLoader configLoader, File testConfig) {
        try
        {
            Class c = configLoader.getClass();
            Field field = c.getDeclaredField("file");
            field.setAccessible(true);
            field.set(configLoader, testConfig);

            Method method = c.getDeclaredMethod("loadConfig");
            method.setAccessible(true);
            method.invoke(configLoader);
        }
        catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void saveProducts()
    {
        loadConfig();
        AmazonProduct ap = new AmazonProduct();
        EbayProduct ep = new EbayProduct();
        ap.setProductId("B011N78DMA");
        //ep.setProductId("282775961177");//Abgelaufenes Produkt
        ep.setProductId(ProductSearcher.searchEbayProductList("Logitech g920").get(0).getProductId());
        //int id = DBProduct.saveProducts(ep);
        //Assert.assertTrue(id != -1);
    }

    /*@Test
    public void loadProductsExisting()
    {
        loadConfig();
        List<Product> products = DBProduct.loadSiteProducts(2);
        Assert.assertFalse(products.isEmpty());
        for(Product p : products)
        {
            System.out.println(p.getTitle());
        }
    }

    @Test
    public void loadProductsNotExisting()
    {
        loadConfig();
        List<Product> products = DBProduct.loadSiteProducts(100);
        Assert.assertTrue(products.isEmpty());
    }

    @Test
    public void addToPriceHistory()
    {
        loadConfig();
        List<Product> products = DBProduct.loadSiteProducts(2);
        Assert.assertFalse(products.isEmpty());
        Product p = products.get(1);
        PriceHistory ph = new PriceHistory(p.getPrice());
        Assert.assertTrue(DBProduct.addToPriceHistory(p, ph));
    }

    @Test
    public void loadPriceHistory()
    {
        AmazonProduct product = new AmazonProduct();
        product.setProductId("B011N78DMA");
        List<PriceHistory> ph = DBProduct.loadPriceHistory(product);
        Assert.assertFalse(ph.isEmpty());
    }*/
}
