package searchpp.sites.products;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.ProductGroup;
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
    //                                                                       price in cent                     price in cent
    public String get(@QueryParam("search_text") String search, @QueryParam("price_min") int min, @QueryParam("price_max") int max, @QueryParam("used") boolean used) {
        // no string, return
        if (search == null) {
            JSONObject object = new JSONObject();
            object.put("data", new JSONArray());
            object.put("elements", 0);
            return object.toJSONString();
        }

        //control price
        boolean price = false;
        if (min > 0 || max > 0)
            price = true;
        if (price) {
            if (max < 0)
                max = 0;
            if (min < 0)
                min = 0;

            if (min > 0 && max == 0)
                max = Integer.MAX_VALUE;

            if (min > max) {
                int tmp = min;
                min = max;
                max = tmp;
            }
        }


        //look for amazon products
        List<AmazonProduct> amazonProductList;
        if (price)
            amazonProductList = ProductSearcher.searchAmazonProductList(search, true, min * .01, max * 0.01);
        else
            amazonProductList = ProductSearcher.searchAmazonProductList(search, true);

        // no products, return
        if (amazonProductList.size() == 0) {
            JSONObject object = new JSONObject();
            object.put("data", new JSONArray());
            object.put("elements", 0);
            return object.toJSONString();
        }

        //sort products
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

        //keep the 10 best products
        for (int i = amazonProductList.size() - 11; i >= 0; --i)
            amazonProductList.remove(i);
        //remove products without rating
        while (amazonProductList.get(0).getRating() == null)
            amazonProductList.remove(0);

        //sort product list to height - low rating
        amazonProductList.sort(new Comparator<AmazonProduct>() {
            @Override
            public int compare(AmazonProduct o1, AmazonProduct o2) {
                return -1 * (o1.getRating().compareTo(o2.getRating()));
            }
        });

        //group products and download ebay products
        ProductGroup[] productGroups = new ProductGroup[amazonProductList.size()];
        for (int i = 0; i < productGroups.length; ++i) {
            productGroups[i] = new ProductGroup();//(amazonProductList.get(i).getGlobalId());
            productGroups[i].add(amazonProductList.get(i));
            productGroups[i].addAll(ProductSearcher.searchEbayProductList(Long.toString(amazonProductList.get(i).getEan())));
        }

        for(ProductGroup grp : productGroups)
        {
            grp.saveToDatabase();
        }

        //convert to json
        JSONArray array = new JSONArray();
        for (ProductGroup products : productGroups) {
            //remove all products out of price range
            if (price)
                products.setPrice(min, max);
            if (!used)
                products.removeUsed();
            array.add(products.getJsonObject());
        }
        JSONObject object = new JSONObject();
        object.put("data", array);
        object.put("elements", array.size());

        return object.toJSONString();
    }
}