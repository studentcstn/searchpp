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
}
