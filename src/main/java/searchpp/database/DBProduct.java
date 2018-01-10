package searchpp.database;

import com.mysql.cj.api.xdevapi.SqlStatement;
import com.mysql.cj.xdevapi.SqlDataResult;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.EbayProduct;
import searchpp.model.products.PriceHistory;
import searchpp.model.products.Product;
import searchpp.services.ProductSearcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBProduct
{
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
                        aproduct = ProductSearcher.searchAmazonProduct(pid);
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

    public static int saveProducts(Product ... p)
    {
        try {
            String sql = "INSERT INTO products() VALUES();";
            int id = DBConnection.getConnection().insert(sql);
            for (Product product : p) {
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
            return id;
        }
        catch(SQLException ex)
        {
            return -1;
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
