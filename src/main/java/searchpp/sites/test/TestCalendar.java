package searchpp.sites.usr.token;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import searchpp.database.DBUser;
import searchpp.model.user.User;
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

import java.time.ZonedDateTime;

import searchpp.model.config.Api;
import searchpp.utils.ConfigLoader;
import searchpp.services.Calendar;

@Path("test/calendar")
public class TestCalendar {
    @Context Request request;
    @Context Response response;

    @GET
    public void insert() {
        String token = "118361281203547310729";

        if (token == null) {
            System.out.println(request.getParameter("error"));
            return;
        }

        User u = DBUser.loadUserByToken(token);

        if (u == null) {
            System.out.println(request.getParameter("error"));
            return;
        }

        ZonedDateTime start = ZonedDateTime.parse("2018-02-15T12:30:00Z[Europe/Berlin]");
        ZonedDateTime end = ZonedDateTime.parse("2018-02-16T12:30:00Z[Europe/Berlin]");
        String id = Calendar.insert(u, start, end, "TEST", "test searchpp");
        System.out.println(id);
    }
}
