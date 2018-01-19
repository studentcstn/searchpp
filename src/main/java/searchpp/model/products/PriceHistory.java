package searchpp.model.products;

import org.json.simple.JSONObject;
import searchpp.model.json.JsonObject;

import java.util.Date;

public class PriceHistory implements JsonObject
{
    private Date _date;
    private double _price;
    public PriceHistory(Date date, double price)
    {
        _date = date;
        _price = price;
    }

    public PriceHistory(double price)
    {
        _date = new Date();
    }
    public Date getDate()
    {
        return _date;
    }
    public double getPrice()
    {
        return _price;
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject priceHistory = new JSONObject();
        priceHistory.put("date", getDate().toString());
        priceHistory.put("price", getPrice());
        return priceHistory;
    }
}
