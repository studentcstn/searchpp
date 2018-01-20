package searchpp.sites.usr;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import searchpp.model.config.Api;
import searchpp.utils.ConfigLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Path("usr")
public class Usr {
    @Context Request request;
    @Context Response response;

    @GET
    public void get() {
        String clientId = ConfigLoader.getConfig("google", Api.clientID);

        String scope = "openid%20email%20https://www.googleapis.com/auth/calendar";
        String redirectUri = "http://localhost:8080/myapp/usr/token";

        // TODO security: generate `state` token
        StringBuilder url = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
            .append("?client_id=").append(clientId)
            .append("&response_type=code")
            .append("&scope=").append(scope)
            .append("&redirect_uri=").append(redirectUri)
            .append("&access_type=offline")
            .append("&approval_prompt=force");

        try
        {
            response.sendRedirect(url.toString());
        } catch(IOException e)
        {
            // TODO useful error message
            System.out.println("Something went wrong: " + e);
        }
    }
}
