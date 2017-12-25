package searchpp.sites.products;

import com.mysql.cj.xdevapi.JsonArray;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.Product;
import searchpp.services.ProductSearcher;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.List;

@Path("products")
public class Products {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@QueryParam("search_text") String search, @QueryParam("price_min") double min, @QueryParam("price_max") double max, @QueryParam("used") boolean used) {
        if (search == null)
            return new JsonArray().toString();
        List<AmazonProduct> amazonProductList = ProductSearcher.searchAmazonProductList(search);

        amazonProductList.sort(new Comparator<AmazonProduct>() {
            @Override
            public int compare(AmazonProduct o1, AmazonProduct o2) {
                if (o1.getRating() == null && o2.getRating() == null)
                    return 0;
                if (o1.getRating() == null)
                    return -1;
                if (o2.getRating() == null)
                    return 1;
                return o1.getRating().compareTo(o2.getRating());
            }
        });

        for (int i = amazonProductList.size() - 11; i >= 0; --i)
            amazonProductList.remove(i);
        while (amazonProductList.get(0).getRating() == null)
            amazonProductList.remove(0);

        amazonProductList.sort(new Comparator<AmazonProduct>() {
            @Override
            public int compare(AmazonProduct o1, AmazonProduct o2) {
                return -1 * (o1.getRating().compareTo(o2.getRating()));
            }
        });

        for (AmazonProduct amazonProduct : amazonProductList)
            System.out.println(amazonProduct.toString());

        StringBuilder stringBuilder = new StringBuilder();
        for (AmazonProduct amazonProduct : amazonProductList)
            stringBuilder.append(amazonProduct.toString()).append('\n');

        return stringBuilder.toString();
    }
}