package searchpp.sites.usr.token;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import searchpp.database.DBUser;
import searchpp.model.user.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.*;

import searchpp.model.config.Api;
import searchpp.utils.ConfigLoader;

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
            // TODO log
            System.out.println(request.getParameter("error"));
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
            // TODO log and response
            System.out.println(e);
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(body);
        } catch (ParseException e) {
            // TODO log and response
            throw new RuntimeException("Unable to parse json " + body);
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
            // TODO log and response
            System.out.println(e);
            return;
        }

        try {
            jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            // TODO log and response
            System.out.println(e);
            return;
        }

        String token = (String)jsonObject.get("id");
        String email = (String)jsonObject.get("email");

        User u = DBUser.createUserOrUpdate(email, token, accessToken, refreshToken);

        try {
            // TODO temporary redirect
            response.sendRedirect("http://localhost:8080/index.html#/?token=" + u.getToken());
        } catch(IOException e) {
            // TODO useful error message
            System.out.println("Something went wrong: " + e);
        }
    }
}
