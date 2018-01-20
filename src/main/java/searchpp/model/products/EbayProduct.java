package searchpp.model.products;

import org.json.simple.JSONObject;

/**
 * Created by Tobi on 04.12.2017.
 */
public class EbayProduct extends Product
{
    /**
     * {@link ListingType} of product
     */
    private ListingType _listingType;

    public EbayProduct()
    {}

    public void setListingType(ListingType listingType) {
        _listingType = listingType;
    }

    public ListingType getListingType() {
        return _listingType;
    }

    /**
     * Creates JSON Object from product
     * @return the JSON Object
     */
    @Override
    public JSONObject getJsonObject() {
        JSONObject object = super.getJsonObject();
        object.put("listingType", _listingType.toString());
        return object;
    }
}
