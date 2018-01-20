package searchpp.database;

import searchpp.model.products.ProductGroup;
import searchpp.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBUser
{
    public static User loadUserByToken(String token)
    {
        try
        {
            User user = null;
            String sql = "SELECT id, email, token, access_token, refresh_token FROM users WHERE token = '" + token + "';";
            ResultSet result = DBConnection.getConnection().query(sql);
            if(result == null)
            {
                return user;
            }
            if (result.first())
            {
                user = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            }
            result.close();
            return user;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR: DBUser.loadUserByToken: " + ex.getMessage());
            return null;
        }
    }

    public static User createUserOrUpdate(String email, String token, String accessToken, String refreshToken)
    {
        String sql = "INSERT INTO users (email, token, access_token, refresh_token) VALUES (?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE email=VALUES(email), token=VALUES(token), " +
                "access_token=VALUES(access_token), refresh_token=VALUES(refresh_token);";
        int id = DBConnection.getConnection().insert(sql, email, token, accessToken, refreshToken);

        //Error occurred
        if (id == -1)
        {
            return null;
        }
        //User already in db
        else if (id == -2)
        {
            return loadUserByToken(token);
        }
        //User inserted
        else
        {
            return new User(id, email, token, accessToken, refreshToken);
        }
    }

    public static String getEventId(User u, int productId) {
        String sql = String.format(
            "SELECT event_id FROM usr_product_watch WHERE user_id = '%d' AND product_id = '%d'",
            u.getId(),
            productId
        );
        ResultSet result = DBConnection.getConnection().query(sql);
        try {
            if (result != null && result.next()) {
                return result.getString(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("ERR: DBUser.getEventId: " + e.getMessage());
            return null;
        }
    }

    public static List<String> getEventIds(User u) {
        List<String> eventIds = new ArrayList<String>();
        String sql = "SELECT DISTINCT event_id FROM usr_product_watch WHERE user_id = " + u.getId() + ";";
        try {
            ResultSet result = DBConnection.getConnection().query(sql);
            while (result.next()) {
                eventIds.add(result.getString(1));
            }
        } catch (SQLException ex) {
            System.err.println("ERR: DBUser.getAllEventIds: " + ex.getMessage());
        }
        return eventIds;
    }

    public static List<ProductGroup> loadWatchedProducts(User u)
    {
        List<ProductGroup> groups = new ArrayList<>();
        String sql = "SELECT DISTINCT product_id, date_from, date_to FROM usr_product_watch WHERE user_id = " + u.getId() + ";";
        try
        {
            ResultSet result = DBConnection.getConnection().query(sql);
            if(result == null)
            {
                return groups;
            }
            while (result.next())
            {
                int gid = result.getInt(1);
                ProductGroup grp = DBProduct.loadProductGroup(gid);
                grp.setDateFrom(result.getDate(2));
                grp.setDateTo(result.getDate(3));
                if(grp != null)
                {
                    groups.add(grp);
                }
            }
            result.close();
        }
        catch (SQLException ex)
        {
            System.err.println("ERR: DBUser.loadWatchedProducts: " + ex.getMessage());
        }
        return groups;
    }

    public static boolean addWatchedProduct(User u, int gid, String eventId, LocalDate from, LocalDate to)
    {
        String sql = "INSERT INTO usr_product_watch(user_id, product_id, event_id, date_from, date_to)" +
                " VALUES (?, ?, ?, ?, ?);";
        return DBConnection.getConnection().insert(sql, u.getId(), gid, eventId, from, to) != -1;
    }

    public static boolean changeWatchedProduct(User u, int gid, String eventId, LocalDate from, LocalDate to)
    {
        String sql = "UPDATE usr_product_watch SET event_id = ?, date_from = ?, date_to = ? WHERE user_id = ? AND product_id = ?";
        return DBConnection.getConnection().insert(sql, eventId, from, to, u.getId(), gid) != -1;
    }

    public static boolean changeWatchedProduct(User u, int gid, LocalDate from, LocalDate to)
    {
        String sql = "UPDATE usr_product_watch SET date_from = ?, date_to = ? WHERE user_id = ? AND product_id = ?";
        return DBConnection.getConnection().insert(sql, from, to, u.getId(), gid) != -1;
    }

    public static boolean removeWatchedProduct(User u, int gid)
    {
        String sql = "DELETE FROM usr_product_watch WHERE user_id = "
                + u.getId() + " AND product_id = " + gid + ";";
        return DBConnection.getConnection().execute(sql);
    }

    public static boolean removeAllWatchedProducts(User u)
    {
        boolean result = false;
        String sql = "DELETE FROM usr_product_watch WHERE user_id = " + u.getId() + ";";
        return DBConnection.getConnection().execute(sql);
    }
}
