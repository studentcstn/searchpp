package searchpp.sites.usr.token;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import searchpp.database.DBUser;
import searchpp.model.config.Api;
import searchpp.model.user.User;
import searchpp.utils.ConfigLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("usr/token")
public class Token {
    @Context Request request;
    @Context Response response;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public void get() {
        String clientId = ConfigLoader.getConfig("google", Api.clientID);
        String clientSecret = ConfigLoader.getConfig("google", Api.secretKey);

        String redirectUri = "http://localhost:8080/myapp/usr/token";

        if (request.getParameter("error") != null) {
            response.setStatus(500);
            System.err.println("ERR: user/token: error: " + request.getParameter("error"));
            return;
        }

        String code = request.getParameter("code");

        String body = null;

        try {
            body = Jsoup.connect("https://www.googleapis.com/oauth2/v4/token")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .data("code", code)
                .data("client_id", clientId)
                .data("client_secret", clientSecret)
                .data("redirect_uri", redirectUri)
                .data("grant_type", "authorization_code")
                .execute()
                .body();
        } catch (IOException e) {
            response.setStatus(500);
            System.err.println("ERR: user/token: error get access token");
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(body);
        } catch (ParseException e) {
            response.setStatus(500);
            System.err.println("ERR: user/token: unable to parse access token response: " + e.getMessage());
            return;
        }

        String accessToken = (String)jsonObject.get("access_token");
        String refreshToken = (String)jsonObject.get("refresh_token");
        request.getSession().setAttribute("access_token", accessToken);

        String json = null;

        try {
            json = Jsoup.connect("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken)
                .ignoreContentType(true)
                .execute()
                .body();
        } catch (IOException e) {
            response.setStatus(500);
            System.err.println("ERR: user/token: unable to get user information with new access token: " + e.getMessage());
            return;
        }

        try {
            jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            response.setStatus(500);
            System.err.println("ERR: user/token: unable to parse user information response: " + e.getMessage());
            return;
        }

        String token = (String)jsonObject.get("id");
        String email = (String)jsonObject.get("email");

        User u = DBUser.createUserOrUpdate(email, token, accessToken, refreshToken);

        if(u == null) {
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE);
        }

        try {
            response.sendRedirect("http://localhost:8080/index.html#/?token=" + u.getToken());
        } catch(IOException e) {
            response.setStatus(500);
            System.err.println("ERR: user/token: unable to redirect to index.html: " + e.getMessage());
            return;
        }
    }
}
