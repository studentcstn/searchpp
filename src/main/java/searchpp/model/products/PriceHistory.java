package searchpp.model.products;

import org.json.simple.JSONObject;

import java.util.Date;

public class PriceHistory
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
    public JSONObject getJsonItem(){
        JSONObject priceHistory = new JSONObject();
        priceHistory.put("date", getDate());
        priceHistory.put("price", getPrice());


        return priceHistory;
    }
}
