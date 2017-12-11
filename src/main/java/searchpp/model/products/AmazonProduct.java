package searchpp.model.products;

/**
 * Created by Tobi on 04.12.2017.
 */
public class AmazonProduct extends Product
{
    private int _salesRank;
    private long _ean;
    private String _manufacturer;
    private String _model;
    private double _rating;

    public AmazonProduct()
    {}

    public void setSalesRank(int salesRank)
    {
        _salesRank = salesRank;
    }
    public int getSalesRank() {
        return _salesRank;
    }


    public void setEan(long ean)
    {
        _ean = ean;
    }
    public long getEan() {
        return _ean;
    }


    public void setManufacturer(String manufacturer)
    {
        _manufacturer = manufacturer;
    }
    public String getManufacturer() {
        return _manufacturer;
    }


    public void setModel(String model)
    {
        _model = model;
    }
    public String getModel() {
        return _model;
    }

    public void setRating(double rating)
    {
        _rating = rating;
    }
    public double getRating() {
        return _rating;
    }
}
