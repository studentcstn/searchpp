package searchpp.sites.usr.token;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("usr/token")
public class Token {

    //todo edit
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return "token ist working";
    }
}