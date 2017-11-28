package searchpp.usr;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("usr")
public class Usr {

    @GET
    @Produces(MediaType.TEXT_PLAIN) //todo change
    public String get() {
        return "usr get";
    }
}