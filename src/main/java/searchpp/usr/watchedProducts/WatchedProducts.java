package searchpp.usr.watchedProducts;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("usr/watchedProducts")
public class WatchedProducts {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return "watched products get";
    }

    @POST
    public void post() {

    }

    @DELETE
    public void delete() {

    }
}