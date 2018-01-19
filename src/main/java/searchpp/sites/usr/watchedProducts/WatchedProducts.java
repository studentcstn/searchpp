package searchpp.sites.usr.watchedProducts;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import searchpp.database.DBUser;
import searchpp.model.products.ProductGroup;
import searchpp.model.user.User;
import searchpp.services.Calendar;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStreamReader;
import java.time.*;
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
    public void post(@PathParam("userToken") String userToken)
    {
        boolean wasSuccessful = false;
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject result = (JSONObject) parser.parse(new InputStreamReader(request.getInputStream()));
            if(result.containsKey("product_id") && result.containsKey("date_to"))
            {
                int product_id = (int)(long)result.get("product_id");
                LocalDate date_to = LocalDate.parse((String)result.get("date_to"));
                LocalDate date_from = LocalDate.now();
                if(result.containsKey("date_from"))
                {
                    date_from = LocalDate.parse((String)result.get("date_from"));
                }
                User user = DBUser.loadUserByToken(userToken);
                if (user != null)
                {
                    wasSuccessful = DBUser.addWatchedProduct(user, product_id, date_from, date_to);
                    String eventId = Calendar.insert(
                        user,
                        ZonedDateTime.of(date_from, LocalTime.now(), ZoneId.of("Europe/Berlin")),
                        ZonedDateTime.of(date_to, LocalTime.now(), ZoneId.of("Europe/Berlin")),
                        String.format("product_id %d", product_id),
                        String.format("Überwachung des Produkts mit der ID %d", product_id)
                    );
                    if (eventId == null) {
                        // TODO
                        System.out.println("Insert failed");
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
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }
    }

    @DELETE
    public void delete(@PathParam("userToken") String userToken)
    {
        boolean wasSuccessful = false;
        User user = DBUser.loadUserByToken(userToken);
        if (user != null)
        {
            wasSuccessful = DBUser.removeAllWatchedProducts(user);
        }
        if(!wasSuccessful)
        {
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }
    }
}