package searchpp.database;

import searchpp.model.products.*;
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
                "AND product_id = " + gIDd + ";";
        try
        {
            ResultSet result = DBConnection.getConnection().query(sql);
            if(result.next())
            {
                return result.getString(1);
            }
            return null;
        }
        catch(SQLException ex)
        {
            return null;
        }
    }
    public static List<Product> loadSiteProducts(int gId)
    {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT site_id, platform FROM product_to_site WHERE product_id = " + gId + ";";
        try
        {
            ResultSet result = DBConnection.getConnection().query(sql);
            while (result.next())
            {
                String plattform = result.getString(2);
                String pid = result.getString(1);
                switch (plattform)
                {
                    case "amazon":
                        AmazonProduct aproduct;
                        //Fehlt: Fehler
                        aproduct = ProductSearcher.searchAmazonProduct(pid, false);
                        aproduct.setGlobalId(gId);
                        products.add(aproduct);
                        break;
                    case "ebay":
                        EbayProduct eproduct = new EbayProduct();
                        eproduct.setProductId(pid);
                        eproduct = ProductSearcher.searchEbayProduct(eproduct);
                        eproduct.setGlobalId(gId);
                        products.add(eproduct);
                        break;
                }
            }
        }
        catch(SQLException ex)
        {

        }
        return products;
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
            if(result.next())
            {
                id = result.getInt(1);
            }
            else
            {
                sql = "INSERT INTO products() VALUES();";
                id = DBConnection.getConnection().insert(sql);
            }

            for (Product product : group) {
                String site = null;
                if (product instanceof AmazonProduct) {
                    site = "amazon";
                } else if (product instanceof EbayProduct) {
                    site = "ebay";
                }
                if (site != null) {
                    sql = "INSERT INTO product_to_site(product_id, site_id, platform) " +
                            "VALUES (" + id + ", '" + product.getProductId() + "', '" + site + "');";
                    DBConnection.getConnection().execute(sql);
                }
            }

            group.setGlobalId(id);
            return true;
        }
        catch(SQLException ex)
        {
            return false;
        }
    }

    public static boolean addToPriceHistory(Product p, PriceHistory ph)
    {
        boolean result = false;
        String sql = "INSERT INTO site_price_history(site_id, price, date) " +
                "VALUES ('" + p.getProductId() + "', " + ph.getPrice() + ", ?);";
        try
        {
            result = DBConnection.getConnection().executeDateParameter(sql, ph.getDate());
        }
        catch(SQLException ex)
        {

        }
        return result;
    }

    public static List<Product> loadAllWatchedProducts()
    {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT DISTINCT product_id FROM usr_product_watch;";
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

    public static List<PriceHistory> loadPriceHistory(Product p)
    {
        ArrayList<PriceHistory> ph = new ArrayList<>();
        String sql = "SELECT price, date FROM site_price_history WHERE site_id = '" + p.getProductId() + "';";
        try
        {
            ResultSet result = DBConnection.getConnection().query(sql);
            while (result.next())
            {
                ph.add(new PriceHistory(result.getDate(2), result.getDouble(1)));
            }
        }
        catch(SQLException ex)
        {

        }
        return ph;
    }


}
