package searchpp.sites.usr.token;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import searchpp.database.DBUser;
import searchpp.model.user.User;
import searchpp.services.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.time.ZonedDateTime;

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
