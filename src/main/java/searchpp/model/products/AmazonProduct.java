package searchpp.model.products;

/**
 * Created by Tobi on 04.12.2017.
 */
public class AmazonProduct extends Product
{
    private int _salesRank;
    private int _ean;
    private String _manufacturer;
    private String _model;
    private double _rating;

    public void setSalesRank(int salesRank)
    {
        _salesRank = salesRank;
    }

    public void setEan(int ean)
    {
        _ean = ean;
    }

    public void setManufacturer(String manufacturer)
    {
        _manufacturer = manufacturer;
    }

    public void setModel(String model)
    {
        _model = model;
    }

    public void setRating(double rating)
    {
        _rating = rating;
    }
}
