package searchpp.model.user;

import searchpp.database.DBUser;
import searchpp.model.products.Product;

import java.util.List;

public class User
{
    private int _id;
    private String _email;
    private List<Product> _watchedProducts;

    public int getId()
    {
        return _id;
    }

    public String getEmail()
    {
        return _email;
    }

    public List<Product> getWatchedProducts()
    {
        return _watchedProducts;
    }

    public User(int id, String email)
    {
        this._id = id;
        this._email = email;
    }

    public boolean addWatchedProduct(Product p)
    {
        boolean result = DBUser.addWatchedProduct(this, p);
        loadWatchedProducts();
        return result;
    }

    public boolean removeWatchedProduct(Product p)
    {
        boolean result = DBUser.removeWatchedProduct(this, p);
        loadWatchedProducts();
        return result;
    }

    public void loadWatchedProducts()
    {
        _watchedProducts = DBUser.loadWatchedProducts(this);
    }
}
