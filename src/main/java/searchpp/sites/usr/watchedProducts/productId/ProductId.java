package searchpp.sites.usr.watchedProducts.productId;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import searchpp.database.DBProduct;
import searchpp.database.DBUser;
import searchpp.model.products.PriceHistory;
import searchpp.model.user.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

@Path("usr/{userToken}/watchedProducts/{productId}")
public class ProductId {

    @Context
    Request request;
    @Context
    Response response;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("userToken") String userToken, @PathParam("productId") int productId) {
        User user = DBUser.loadUserByToken(userToken);
        JSONArray array = new JSONArray();
        if (user != null)
        {
            List<PriceHistory> priceHistoryList = DBProduct.loadPriceHistory(productId);
            for (PriceHistory priceHist : priceHistoryList) {
                array.add(priceHist.getJsonObject());
            }
        }
        JSONObject object = new JSONObject();
        object.put("elements", array.size());
        object.put("data", array);

        return object.toJSONString();
    }

    @PUT
    public void put(@PathParam("userToken") String userToken, @PathParam("productId") int product_id) {
        boolean wasSuccessful = false;
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject result = (JSONObject) parser.parse(new InputStreamReader(request.getInputStream()));
            if(result.containsKey("date_to"))
            {
                LocalDate date_to = LocalDate.parse((String)result.get("date_to"));
                LocalDate date_from = LocalDate.now();
                if(result.containsKey("date_from"))
                {
                    date_from = LocalDate.parse((String)result.get("date_from"));
                }
                User user = DBUser.loadUserByToken(userToken);
                if (user != null)
                {
                    wasSuccessful = DBUser.changeWatchedProduct(user, product_id, date_from, date_to);
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        if(!wasSuccessful)
        {
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }
    }

    @DELETE
    public void delete(@PathParam("userToken") String userToken, @PathParam("productId") int productId) {
        boolean wasSuccessful = false;
        User user = DBUser.loadUserByToken(userToken);
        if (user != null)
        {
            wasSuccessful = DBUser.removeWatchedProduct(user, productId);
        }
        if(!wasSuccessful)
        {
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }
    }
}