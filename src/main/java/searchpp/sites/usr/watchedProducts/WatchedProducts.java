package searchpp.sites.usr.watchedProducts;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBUser;
import searchpp.model.products.Product;
import searchpp.model.user.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("usr/{userToken}/watchedProducts")
public class WatchedProducts
{
    @Context
    Request request;
    @Context
    Response response;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("userToken") long userToken)
    {
        //todo get user with userToken from db / load WatchedProducts with userToken
        //Email?
        User user = DBUser.loadUser("");
        List<Product> products = DBUser.loadWatchedProducts(user);

        JSONArray array = new JSONArray();
        for(Product p : products)
            array.add(p.getJsonObject());

        JSONObject object = new JSONObject();
        object.put("elements", products.size());
        object.put("data", array);

        return object.toJSONString();
    }

    @POST
    public void post(@PathParam("userToken") long userToken, @QueryParam("product_id") long product_id, @QueryParam("date_to") Date date_to, @QueryParam("date_from") Date date_from)
    {
        //todo save product_id to user with userToken
    }

    @DELETE
    public void delete(@PathParam("userToken") long userToken)
    {
        //todo remove all watched products from user with userToken
    }
}