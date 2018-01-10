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


}
