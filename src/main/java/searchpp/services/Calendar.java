package searchpp.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import searchpp.model.user.User;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calendar {
    private static JSONObject convertZonedDateTimeToJSONObject(ZonedDateTime dt) {
        JSONObject json = new JSONObject();

        json.put("dateTime", dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        json.put("timeZone", dt.getZone().getId());

        return json;
    }

    private static String insertUpdate(String eventId, User user, ZonedDateTime start, ZonedDateTime end, String title, String description) {
        String url;
        if (eventId == null) {
            url = String.format(
                "https://www.googleapis.com/calendar/v3/calendars/primary/events?access_token=%s",
                user.getAccessToken()
            );
        } else {
            url = String.format(
                "https://www.googleapis.com/calendar/v3/calendars/primary/events/%s?access_token=%s",
                eventId,
                user.getAccessToken()
            );
        }

        String responseBody = null;

        JSONObject startObject = convertZonedDateTimeToJSONObject(start);
        JSONObject endObject = convertZonedDateTimeToJSONObject(end);

        JSONObject event = new JSONObject();
        event.put("start", startObject);
        event.put("end", endObject);
        event.put("summary", title);
        event.put("description", description);

        Connection.Method method;
        if (eventId == null) {
            method = Connection.Method.POST;
        } else {
            method = Connection.Method.PUT;
            event.put("eventId", eventId);
        }

        try {
            responseBody = Jsoup.connect(url)
                .ignoreContentType(true)
                .method(method)
                .header("Content-Type", "application/json")
                .requestBody(event.toString())
                .execute()
                .body();
        } catch (IOException e) {
            System.err.println("Calendar: event insert/udpate: " + e.getMessage());
            return null;
        }

        JSONObject response = null;
        try {
            response = (JSONObject)new JSONParser().parse(responseBody);
        } catch (ParseException e) {
            System.err.println("Calendar: event insert/update: parse response: " + e.getMessage());
            return null;
        }

        return (String)response.get("id");
   }

    public static String insert(User user, ZonedDateTime start, ZonedDateTime end, String title, String description) {
        return insertUpdate(null, user, start, end, title, description);
    }

    public static String update(User user, ZonedDateTime start, ZonedDateTime end, String title, String description, String eventId) {
        return insertUpdate(eventId, user, start, end, title, description);
    }

    public static boolean delete(User user, String eventId) {
        String url = String.format(
            "https://www.googleapis.com/calendar/v3/calendars/primary/events/%s?access_token=%s",
            eventId,
            user.getAccessToken()
        );

        try {
            Jsoup.connect(url)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .header("Content-Type", "application/json")
                .execute();
        } catch (IOException e) {
            System.err.println("Calendar: event delete: " + e.getMessage());
            return false;
        }

        return true;
    }
}
