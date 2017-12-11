package searchpp.sites.usr.watchedProducts.productId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("usr/watchedProducts/{productId}")
public class ProductId {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return "product id get";
    }

    @PUT
    public void put() {

    }

    @DELETE
    public void delete() {

    }
}