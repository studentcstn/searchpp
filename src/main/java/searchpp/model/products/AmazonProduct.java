package searchpp.model.products;

import org.json.simple.JSONObject;

/**
 * Created by Tobi on 04.12.2017.
 */
public class AmazonProduct extends Product
{
    /**
     * Sales rank of a product from amazon
     */
    private int _salesRank;
    /**
     * EAN of the product
     */
    private long _ean;
    /**
     * Manufacturer of the product
     */
    private String _manufacturer;
    /**
     * Model of the product
     */
    private String _model;
    /**
     * The rating from amazon
     */
    private AmazonProductRating _rating;

    public AmazonProduct() {}

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
        super.setGlobalId(ean);
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

    /**
     * Creates JSON Object from product
     * @return the JSON Object
     */
    @Override
    public JSONObject getJsonObject(){
        JSONObject object = super.getJsonObject();
        if (_rating != null)
            object.put("rating", _rating.getAverageRating());

        return  object;
    }
}
