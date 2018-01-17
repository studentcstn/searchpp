package searchpp.localservices;

import searchpp.database.DBProduct;
import searchpp.model.products.PriceHistory;
import searchpp.model.products.Product;
import searchpp.model.products.ProductGroup;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class PriceHistoryService extends TimerTask
{
    @Override
    public void run()
    {
        Date dt = new Date();
        List<ProductGroup> groups = DBProduct.loadAllWatchedProducts();
        for(ProductGroup pg : groups)
        {
            PriceHistory ph = new PriceHistory(dt, pg.getMinPrice());
            DBProduct.addToPriceHistory(pg.getProductID(), ph);
        }
    }
}
