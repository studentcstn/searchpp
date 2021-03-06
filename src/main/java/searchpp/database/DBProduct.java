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
    /**
     * Load the Amazon asin from db with a global id
     * @param gIDd the global id to search for
     * @return the asin if in db, otherwise null
     */
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

    /**
     * Save a ProductGroup to database and set its global id
     * Only the first product in list (if amazonproduct) will be saved
     * @param group the group to save
     * @return true if it was saved successful, otherwise false
     */
    public static boolean saveProducts(ProductGroup group)
    {
        if(group.size() == 0) return false;
        String sql;
        int id = 0;
        try {
            //Check if the product is already in database
            //If already in db, select the gid and set it
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
                //Insert the amazonproduct to db and set returned id
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

    /**
     * Add the actual price (in PriceHistory) for the product with global id to db
     * @param gid the global id of the product
     * @param ph the priceHistory to add
     * @return true if successful, otherwise false
     */
    public static boolean addToPriceHistory(int gid, PriceHistory ph)
    {
        String sql = "INSERT INTO site_price_history(product_id, price, date) VALUES (?, ?, ?);";
        return DBConnection.getConnection().insert(sql, gid, ph.getPrice(), ph.getDate()) >= 0;
    }

    /**
     * Load a ProductGroup for a global id. This will load the amazonproduct from the db and webservice,
     * and will add the corresponding products from ebay webservice
     * @param gid the global id to search for
     * @return the loaded ProductGroup. If the product could not be retrieved, the list will be empty.
     */
    public static ProductGroup loadProductGroup(int gid)
    {
        String asin = DBProduct.loadAmazonProduct(gid);
        ProductGroup grp = new ProductGroup();
        if(asin != null)
        {
            AmazonProduct prod = ProductSearcher.searchAmazonProduct(DBProduct.loadAmazonProduct(gid), false);
            grp.setGlobalId(gid);
            grp.add(prod);
            grp.addAll(ProductSearcher.searchEbayProductList(Long.toString(prod.getEan())));
        }
        return grp;
    }

    /**
     * Load all products that should be watched now
     * @return a List with productGroups
     */
    public static List<ProductGroup> loadAllWatchedProducts()
    {
        List<ProductGroup> groups = new ArrayList<>();
        String sql = "SELECT DISTINCT product_id FROM usr_product_watch WHERE date_from <= CURRENT_TIMESTAMP AND date_to >= CURRENT_TIMESTAMP;";
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

    /**
     * Load the PriceHistory for a product with global id
     * @param productId the global id to search for
     * @return a List with PriceHistory objects
     */
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
