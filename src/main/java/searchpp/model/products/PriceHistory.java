package searchpp.model.products;

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
}
