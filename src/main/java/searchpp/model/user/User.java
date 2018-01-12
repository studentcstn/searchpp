package searchpp.model.user;

import searchpp.database.DBUser;
import searchpp.model.products.Product;

import java.util.List;

public class User
{
    private int _id;
    private String _email;
    private String _token;
    private String _accessToken;
    private String _refreshToken;
    private List<Product> _watchedProducts;

    public int getId()
    {
        return _id;
    }

    public String getEmail()
    {
        return _email;
    }

    public String getToken()
    {
        return _token;
    }

    public String getAccessToken()
    {
        return _accessToken;
    }

    public String getRefreshToken()
    {
        return _refreshToken;
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

    public User(int id, String email, String token, String accessToken, String refreshToken)
    {
        this._id = id;
        this._email = email;
        this._token = token;
        this._accessToken = accessToken;
        this._refreshToken = refreshToken;
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
