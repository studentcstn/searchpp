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

    public static String insert(User user, ZonedDateTime start, ZonedDateTime end, String title, String description) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events?" +
                    "access_token=" + user.getAccessToken();

        String responseBody = null;

        JSONObject startObject = convertZonedDateTimeToJSONObject(start);
        JSONObject endObject = convertZonedDateTimeToJSONObject(end);

        JSONObject event = new JSONObject();
        event.put("start", startObject);
        event.put("end", endObject);
        event.put("summary", title);
        event.put("description", description);

        try {
            responseBody = Jsoup.connect(url)
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .header("Content-Type", "application/json")
                .requestBody(event.toString())
                .execute()
                .body();
        } catch (IOException e) {
            System.err.println("Calendar: event insert: " + e.getMessage());
            return null;
        }

        JSONObject response = null;
        try {
            response = (JSONObject)new JSONParser().parse(responseBody);
        } catch (ParseException e) {
            System.err.println("Calendar: event insert parse response: " + e.getMessage());
            return null;
        }

        return (String)response.get("id");
    }
}
