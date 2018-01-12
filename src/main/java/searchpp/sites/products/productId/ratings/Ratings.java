package searchpp.sites.products.productId.ratings;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBProduct;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.ProductGroup;
import searchpp.services.ProductSearcher;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("products/{productId}/ratings")
public class Ratings {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("productId") String productID) {
        int gId = Integer.parseInt(productID);
        String asin = DBProduct.loadAmazonProduct(gId);

        AmazonProduct amazonProduct = ProductSearcher.searchAmazonProduct(asin);

        if (amazonProduct.getRating() == null) {
            JSONObject object = new JSONObject();
            object.put("elements", 0);
            object.put("data", new JSONArray());
            return object.toJSONString();
        }


        JSONArray array = amazonProduct.getRating().getJsonList();
        JSONObject object = new JSONObject();
        object.put("allRatings", amazonProduct.getRating().getRatingsCount());
        object.put("elements", array.size());
        object.put("data", array);

        return object.toJSONString();
    }
}
