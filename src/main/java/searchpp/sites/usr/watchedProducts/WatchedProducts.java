package searchpp.sites.usr.watchedProducts;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import searchpp.database.DBUser;
import searchpp.database.DBProduct;
import searchpp.model.products.ProductGroup;
import searchpp.model.user.User;
import searchpp.services.Calendar;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Path("usr/{userToken}/watchedProducts")
public class WatchedProducts
{
    @Context
    Request request;
    @Context
    Response response;

    /**
     * Load the list of watched products for a user
     * @param userToken the token of the user
     * @return the watched products as json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("userToken") String userToken)
    {
        //get user form database
        User user = DBUser.loadUserByToken(userToken);
        JSONArray array = new JSONArray();
        if (user != null)
        {
            //get watched products from database
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

    /**
     * Add a product to watchlist
     * @param userToken the token of the user
     */
    @POST
    public void post(@PathParam("userToken") String userToken)
    {
        boolean wasSuccessful = false;
        try
        {
            JSONParser parser = new JSONParser();
            //Load body parameters
            JSONObject result = (JSONObject) parser.parse(new InputStreamReader(request.getInputStream()));
            //Check, if product_id and date_to was submitted
            if(result.containsKey("product_id") && result.containsKey("date_to"))
            {
                int product_id = (int)(long)result.get("product_id");
                //Parse the date
                LocalDate date_to = LocalDate.parse((String)result.get("date_to"));
                //Use now() if no date_from was submitted
                LocalDate date_from = LocalDate.now();
                if(result.containsKey("date_from"))
                {
                    date_from = LocalDate.parse((String)result.get("date_from"));
                }
                User user = DBUser.loadUserByToken(userToken);
                if (user != null)
                {
                    wasSuccessful = DBUser.addWatchedProduct(user, product_id, "", date_from, date_to);
                    if (wasSuccessful) {
                        ProductGroup group = DBProduct.loadProductGroup(product_id);
                        String eventId = Calendar.insert(
                            user,
                            ZonedDateTime.of(date_from, LocalTime.now(), ZoneId.of("Europe/Berlin")),
                            ZonedDateTime.of(date_to, LocalTime.now(), ZoneId.of("Europe/Berlin")),
                            String.format("(%d) %s", product_id, group.get(0).getTitle()),
                            String.format("Überwachung des Produkts: (%d) %s", product_id, group.get(0).getTitle())
                        );
                        if (eventId != null) {
                            wasSuccessful = DBUser.changeWatchedProduct(user, product_id, eventId, date_from, date_to);
                        }
                    }
                }
            }

        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        if(!wasSuccessful)
        {
            //throw 400 if the request was not successful
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Delete the watchlist of a user
     * @param userToken the token of the user
     */
    @DELETE
    public void delete(@PathParam("userToken") String userToken)
    {
        boolean wasSuccessful = false;
        User user = DBUser.loadUserByToken(userToken);
        if (user != null)
        {
            List<String> eventIds = DBUser.getEventIds(user);
            wasSuccessful = DBUser.removeAllWatchedProducts(user);
            if (wasSuccessful) {
                for (String eventId: eventIds) {
                    if (eventId != "") {
                        Calendar.delete(user, eventId);
                    }
                }
            }
        }
        if(!wasSuccessful)
        {
            //throw 400 if the request was not successful
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }
    }
}
