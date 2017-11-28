package searchpp.products;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("products")
public class Products {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return "products get";
    }
}