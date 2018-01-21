package searchpp.localservices;

import searchpp.database.DBProduct;
import searchpp.model.products.PriceHistory;
import searchpp.model.products.ProductGroup;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class PriceHistoryService extends TimerTask
{
    @Override
    /**
     * Execute the task. Load all watched products, load the price and save it to the db
     */
    public void run()
    {
        Date dt = new Date();
        List<ProductGroup> groups = DBProduct.loadAllWatchedProducts();
        for(ProductGroup pg : groups)
        {
            double min = pg.getMinPrice();
            //Only save price if there is one
            if (!Double.isNaN(min))
            {
                PriceHistory ph = new PriceHistory(dt, min);
                DBProduct.addToPriceHistory(pg.getProductID(), ph);
            }
        }
    }
}
