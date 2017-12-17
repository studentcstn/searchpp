package searchpp.databse;

import org.junit.Assert;
import org.junit.Test;
import searchpp.database.DBUser;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.Product;
import searchpp.model.user.User;

import java.awt.image.DataBufferUShort;
import java.util.List;

public class DbUser_test
{
    @Test
    public void CreateUser()
    {
        User u = DBUser.createUser("test@test.de");
        Assert.assertNotNull(u);
        u = DBUser.createUser("test@test.de");
        Assert.assertNull(u);
    }

    @Test
    public void LoadUser()
    {
        User u = DBUser.loadUser("test@test.de");
        Assert.assertNotNull(u);
    }

    @Test
    public void LoadNotExistingUser()
    {
        User u = DBUser.loadUser("iwas");
        Assert.assertNull(u);
    }

    @Test
    public void LoadWatchedProductsWhenEmpty()
    {
        User u = DBUser.loadUser("test@test.de");
        Assert.assertNotNull(u);
        List<Product> products = DBUser.loadWatchedProducts(u);
        Assert.assertTrue(products.isEmpty());
    }

    @Test
    public void AddWatchedProducts()
    {
        DbProductTest.loadConfig();
        User u = DBUser.loadUser("test@test.de");
        Assert.assertNotNull(u);
        //Produkt 2 MUSS existieren!
        AmazonProduct product = new AmazonProduct();
        product.setGlobalId(2);
        Assert.assertTrue(u.addWatchedProduct(product));
        u.loadWatchedProducts();
        Assert.assertFalse(u.getWatchedProducts().isEmpty());
    }

    @Test
    public void RemoveWatchedProduct()
    {
        DbProductTest.loadConfig();
        User u = DBUser.loadUser("test@test.de");
        Assert.assertNotNull(u);
        //Produkt 2 MUSS existieren!
        AmazonProduct product = new AmazonProduct();
        product.setGlobalId(2);
        Assert.assertTrue(u.removeWatchedProduct(product));
        u.loadWatchedProducts();
        Assert.assertTrue(u.getWatchedProducts().isEmpty());
    }

    @Test
    public void AddRemoveNotExisting()
    {
        DbProductTest.loadConfig();
        User u = DBUser.loadUser("test@test.de");
        Assert.assertNotNull(u);
        AmazonProduct product = new AmazonProduct();
        product.setGlobalId(50);
        Assert.assertFalse(u.addWatchedProduct(product));
        Assert.assertTrue(u.removeWatchedProduct(product));
        u.loadWatchedProducts();
        Assert.assertTrue(u.getWatchedProducts().isEmpty());
    }
}
