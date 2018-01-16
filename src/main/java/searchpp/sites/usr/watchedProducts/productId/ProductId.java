package searchpp.sites.usr.watchedProducts.productId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("usr/{userToken}/watchedProducts/{productId}")
public class ProductId {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("userToken") long userToken, @PathParam("productId") long productId) {

        //todo load products with productId from database
        return "product id get";
    }

    @PUT
    public void put(@PathParam("userToken") long userToken, @PathParam("productId") long productId, @QueryParam("date_to") Date date_to, @QueryParam("date_from") Date date_from) {
        //todo save new date do productId from user with userToken
    }

    @DELETE
    public void delete(@PathParam("userToken") long userToken, @PathParam("productId") long productId) {
        //todo remove product with productId from user with userToken
    }
}