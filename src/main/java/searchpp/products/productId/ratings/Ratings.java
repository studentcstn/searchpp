package searchpp.products.productId.ratings;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("products/{productId}/ratings")
public class Ratings {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return "product ratings get";
    }
}
