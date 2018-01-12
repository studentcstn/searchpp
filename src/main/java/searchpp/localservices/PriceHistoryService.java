package searchpp.localservices;

import searchpp.database.DBProduct;
import searchpp.model.products.PriceHistory;
import searchpp.model.products.Product;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class PriceHistoryService extends TimerTask
{
    @Override
    public void run()
    {
        Date dt = new Date();
        List<Product> products = DBProduct.loadAllWatchedProducts();
        for(Product p : products)
        {
            PriceHistory ph = new PriceHistory(dt, p.getPrice());
            DBProduct.addToPriceHistory(p, ph);
        }
    }
}
