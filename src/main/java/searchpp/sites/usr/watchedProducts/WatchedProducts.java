package searchpp.sites.usr.watchedProducts;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import searchpp.database.DBProduct;
import searchpp.database.DBUser;
import searchpp.model.products.Product;
import searchpp.model.user.User;
import searchpp.services.ConverterToJson;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("usr/watchedProducts")
public class WatchedProducts
{
    @Context
    Request request;
    @Context
    Response response;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get()
    {
        //Email?
        User user = DBUser.loadUser("");
        List<Product> products = DBUser.loadWatchedProducts(user);
        ConverterToJson conv = new ConverterToJson();
        for(Product p : products)
        {
            conv.addJsonList(p.getJsonItem());
        }
        return conv.toString();
    }

    @POST
    public void post()
    {

    }

    @DELETE
    public void delete()
    {

    }
}