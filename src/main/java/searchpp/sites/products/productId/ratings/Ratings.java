package searchpp.sites.products.productId.ratings;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBProduct;
import searchpp.model.products.AmazonProduct;
import searchpp.services.ProductSearcher;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("products/{productId}/ratings")
public class Ratings {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("productId") String productID) {

        //get asin from database
        int gId = Integer.parseInt(productID);
        String asin = DBProduct.loadAmazonProduct(gId);

        //get amazon product with rating
        AmazonProduct amazonProduct = ProductSearcher.searchAmazonProduct(asin, true);
        //maybe no rating
        if (amazonProduct.getRating() == null) {
            throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);
        }

        //convert to json
        JSONArray array = amazonProduct.getRating().getJsonList();
        JSONObject object = new JSONObject();
        object.put("allRatings", amazonProduct.getRating().getRatingsCount());
        object.put("elements", array.size());
        object.put("data", array);

        return object.toJSONString();
    }
}
