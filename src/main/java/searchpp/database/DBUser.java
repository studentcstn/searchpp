package searchpp.database;

import com.mysql.cj.api.xdevapi.SqlStatement;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.Product;
import searchpp.model.products.ProductGroup;
import searchpp.model.user.User;
import searchpp.services.ProductSearcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBUser
{
    public static User loadUserByToken(String token)
    {
        try
        {
            String sql = "SELECT id, email, token, access_token, refresh_token FROM users WHERE token = '" + token + "';";
            ResultSet result = DBConnection.getConnection().query(sql);
            if (result.first())
            {
                return new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
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

    public static User createUserOrUpdate(String email, String token, String accessToken, String refreshToken)
    {
        try
        {
            PreparedStatement stmt = DBConnection
                .getConnection()
                .prepareStatement(
                "INSERT INTO users (email, token, access_token, refresh_token) VALUES (?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE email=VALUES(email), token=VALUES(token), " +
                "access_token=VALUES(access_token), refresh_token=VALUES(refresh_token)"
            );

            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.setString(3, accessToken);
            stmt.setString(4, refreshToken);

            int id = stmt.executeUpdate();
            if (id == -1) {
                return null;
            }

            return new User(id, email, token, accessToken, refreshToken);
        }
        catch(SQLException ex)
        {
            return null;
        }
    }

    public static List<ProductGroup> loadWatchedProducts(User u)
    {
        List<ProductGroup> groups = new ArrayList<>();
        String sql = "SELECT DISTINCT product_id FROM usr_product_watch WHERE user_id = " + u.getId() + ";";
        try
        {
            ResultSet result = DBConnection.getConnection().query(sql);
            while (result.next())
            {
                int gid = result.getInt(1);
                ProductGroup grp = DBProduct.loadProductGroup(gid);
                if(grp != null)
                {
                    groups.add(grp);
                }
            }
        }
        catch (SQLException ex)
        {
            //Log Error
        }
        return groups;
    }

    public static boolean addWatchedProduct(User u, int gid, LocalDate from, LocalDate to)
    {
        boolean result = false;
        String sql = "INSERT INTO usr_product_watch(user_id, product_id, date_from, date_to)" +
                " VALUES ("+u.getId()+", " + gid + ", ?, ?);";
        try
        {
            result = DBConnection.getConnection().executeLocalDateParameter(sql, from, to);
        }
        catch(SQLException ex)
        {

        }
        return result;
    }

    public static boolean changeWatchedProduct(User u, int gid, LocalDate from, LocalDate to)
    {
        boolean result = false;
        String sql = "UPDATE usr_product_watch SET date_from = ?, date_to = ? WHERE user_id = "
                + u.getId() + " AND product_id = " + gid + ";";
        try
        {
            result = DBConnection.getConnection().executeLocalDateParameter(sql, from, to);
        }
        catch(SQLException ex)
        {
            //todo: log error
        }
        return result;
    }

    public static boolean removeWatchedProduct(User u, int gid)
    {
        boolean result = false;
        String sql = "DELETE FROM usr_product_watch WHERE user_id = "
                + u.getId() + " AND product_id = " + gid + ";";
        try
        {
            result = DBConnection.getConnection().execute(sql);
        }
        catch(SQLException ex)
        {

        }
        return result;
    }

    public static boolean removeAllWatchedProducts(User u)
    {
        boolean result = false;
        String sql = "DELETE FROM usr_product_watch WHERE user_id = " + u.getId() + ";";
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
