package searchpp.database;

import searchpp.model.products.AmazonProduct;
import searchpp.model.products.PriceHistory;
import searchpp.model.products.ProductGroup;
import searchpp.services.ProductSearcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBProduct
{
    public static String loadAmazonProduct(int gIDd)
    {
        String sql = "SELECT site_id FROM product_to_site WHERE platform = 'amazon'" +
                " AND product_id = " + gIDd + ";";
        try
        {
            String ret = null;
            ResultSet result = DBConnection.getConnection().query(sql);
            if(result == null)
            {
                return null;
            }
            if(result.next())
            {
                ret = result.getString(1);
            }
            result.close();
            return ret;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR: DBProduct.loadAmazonProduct: " + ex.getMessage());
            return null;
        }
    }

    public static boolean saveProducts(ProductGroup group)
    {
        if(group.size() == 0) return false;
        String sql;
        int id = 0;
        try {
            String query = "SELECT product_id FROM product_to_site WHERE platform = 'amazon'" +
                    " AND site_id = '" + group.get(0).getProductId() + "';";
            ResultSet result = DBConnection.getConnection().query(query);
            if(result == null)
            {
                return false;
            }
            if(result.next())
            {
                id = result.getInt(1);
            }
            else
            {
                sql = "INSERT INTO products() VALUES();";
                id = DBConnection.getConnection().insert(sql);
                if (group.get(0) instanceof AmazonProduct)
                {
                    sql = "INSERT INTO product_to_site(product_id, site_id, platform) " +
                            "VALUES (" + id + ", '" + group.get(0).getProductId() + "', 'amazon');";
                    DBConnection.getConnection().execute(sql);
                }
            }
            result.close();
            group.setGlobalId(id);
            return true;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR: DBProduct.saveProducts: " + ex.getMessage());
            return false;
        }
    }

    public static boolean addToPriceHistory(int gid, PriceHistory ph)
    {
        String sql = "INSERT INTO site_price_history(product_id, price, date) " +
                "VALUES ('" + gid + "', " + ph.getPrice() + ", ?);";
        return DBConnection.getConnection().executeDateParameter(sql, ph.getDate());
    }

    public static ProductGroup loadProductGroup(int gid)
    {
        AmazonProduct prod = ProductSearcher.searchAmazonProduct(DBProduct.loadAmazonProduct(gid), false);
        ProductGroup grp = new ProductGroup();
        grp.setGlobalId(gid);
        grp.add(prod);
        grp.addAll(ProductSearcher.searchEbayProductList(Long.toString(prod.getEan())));
        return grp;
    }

    public static List<ProductGroup> loadAllWatchedProducts()
    {
        List<ProductGroup> groups = new ArrayList<>();
        String sql = "SELECT DISTINCT product_id FROM usr_product_watch WHERE date_to >= CURRENT_TIMESTAMP;";
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
                ProductGroup grp = loadProductGroup(gid);
                if(grp != null)
                {
                    groups.add(grp);
                }
            }
            result.close();
        }
        catch (SQLException ex)
        {
            System.err.println("ERR: DBProduct.loadAllWatchedProducts: " + ex.getMessage());
        }
        return groups;
    }

    public static List<PriceHistory> loadPriceHistory(int productId)
    {
        ArrayList<PriceHistory> ph = new ArrayList<>();
        String sql = "SELECT price, date FROM site_price_history WHERE product_id = " + productId +";";
        try
        {
            ResultSet result = DBConnection.getConnection().query(sql);
            if(result == null)
            {
                return ph;
            }
            while (result.next())
            {
                ph.add(new PriceHistory(result.getTimestamp(2), result.getDouble(1)));
            }
            result.close();
        }
        catch(SQLException ex)
        {
            System.err.println("ERR: DBProduct.loadPriceHistory: " + ex.getMessage());
        }
        return ph;
    }


}
