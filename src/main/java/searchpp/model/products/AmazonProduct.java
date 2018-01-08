package searchpp.model.products;

import org.json.simple.JSONObject;

/**
 * Created by Tobi on 04.12.2017.
 */
public class AmazonProduct extends Product
{
    private int _salesRank;
    private long _ean;
    private String _manufacturer;
    private String _model;
    private AmazonProductRating _rating;

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

    public void setRating(AmazonProductRating rating)
    {
        _rating = rating;
    }
    public AmazonProductRating getRating() {
        return _rating;
    }

    @Override
    public String toString() {
        return getRating().getInternProductRating() + " " + super.toString();
    }

    public JSONObject getJsonItem(){
        JSONObject amazonP = new JSONObject();
        amazonP.put("globalID",super.getGlobalId());
        amazonP.put("productID",super.getProductId());
        amazonP.put("title",super.getTitle());
        amazonP.put("condition",super.getCondition());
        amazonP.put("price",super.getPrice());
        amazonP.put("ean", getEan());
        amazonP.put("manufacturer", getManufacturer());
        amazonP.put("model", getModel());
        amazonP.put("reating", getRating());
        amazonP.put("salesrank", getSalesRank());

        return  amazonP;
    }
}
