package searchpp.sites.products.productId;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("products/{productId}")
public class ProductId {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return "product id get";
    }
}