package searchpp.sites.usr.watchedProducts;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBUser;
import searchpp.model.products.Product;
import searchpp.model.products.ProductGroup;
import searchpp.model.user.User;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
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
    public String get(@PathParam("userToken") String userToken)
    {
        User user = DBUser.loadUserByToken(userToken);
        JSONArray array = new JSONArray();
        if (user != null)
        {
            List<ProductGroup> watchedGroups = DBUser.loadWatchedProducts(user);
            //convert to json
            for (ProductGroup products : watchedGroups) {
                array.add(products.getJsonObject());
            }
        }
        JSONObject object = new JSONObject();
        object.put("data", array);
        object.put("elements", array.size());

        return object.toJSONString();
    }

    @POST
    public void post(@PathParam("userToken") String userToken, @QueryParam("date_to") Date date_to, @QueryParam("date_from") Date date_from)
    {
        int product_id = Integer.parseInt(request.getParameter("product_id"));
        User user = DBUser.loadUserByToken(userToken);
        if (user != null)
        {
            DBUser.addWatchedProduct(user, product_id, date_from, date_to);
        }
    }

    @DELETE
    public void delete(@PathParam("userToken") String userToken)
    {
        User user = DBUser.loadUserByToken(userToken);
        if (user != null)
        {
            DBUser.removeAllWatchedProducts(user);
        }
    }
}