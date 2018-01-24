package searchpp.sites.usr;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

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

        StringBuilder url = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
            .append("?client_id=").append(clientId)
            .append("&response_type=code")
            .append("&scope=").append(scope)
            .append("&redirect_uri=").append(redirectUri)
            .append("&access_type=offline")
            .append("&approval_prompt=force");

        try {
            response.sendRedirect(url.toString());
        } catch(IOException e) {
            response.setStatus(500);
            System.err.println("ERR: usr: redirect to google oauth2: " + e.getMessage());
        }
    }
}
