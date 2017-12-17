package searchpp.database;

import com.mysql.cj.api.xdevapi.SqlStatement;
import searchpp.model.products.Product;
import searchpp.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUser
{
    public static User loadUser(String email)
    {
        try
        {
            String sql = "SELECT id, email FROM users WHERE email = '" + email + "';";
            ResultSet result = DBConnection.getConnection().query(sql);
            if (result.first())
            {
                return new User(result.getInt(1), result.getString(2));
            }
            else
            {
                return null;
            }
        }
        catch(SQLException e)
        {
            return null;
        }
    }

    public static User createUser(String email)
    {
        try
        {
            String sql = "INSERT INTO users(email) VALUES ('" + email + "');";
            int id = DBConnection.getConnection().insert(sql);
            if (id == -1)
            {
                return null;
            }
            return new User(id, email);
        }
        catch(SQLException ex)
        {
            return null;
        }
    }

    public static List<Product> loadWatchedProducts(User u)
    {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id FROM usr_product_watch WHERE user_id = " + u.getId() + ";";
        try
        {
            ResultSet result = DBConnection.getConnection().query(sql);
            while (result.next())
            {
                products.addAll(DBProduct.loadSiteProducts(result.getInt(1)));
            }
        }
        catch (SQLException ex)
        {
            //Log Error
        }
        return products;
    }

    public static boolean addWatchedProduct(User u, Product p)
    {
        boolean result = false;
        String sql = "INSERT INTO usr_product_watch(user_id, product_id) VALUES ("+u.getId()+", " + p.getGlobalId() + ");";
        try
        {
            result = DBConnection.getConnection().execute(sql);
        }
        catch(SQLException ex)
        {

        }
        return result;
    }

    public static boolean removeWatchedProduct(User u, Product p)
    {
        boolean result = false;
        String sql = "DELETE FROM usr_product_watch WHERE user_id = "
                + u.getId() + " AND product_id = " + p.getGlobalId() + ";";
        try
        {
            result = DBConnection.getConnection().execute(sql);
        }
        catch(SQLException ex)
        {

        }
        return result;
    }
}
