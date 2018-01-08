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
    public JSONObject getJsonItem(){
        JSONObject ebayP = new JSONObject();
        ebayP.put("globalID",super.getGlobalId());
        ebayP.put("productID",super.getProductId());
        ebayP.put("title",super.getTitle());
        ebayP.put("condition",super.getCondition());
        ebayP.put("price",super.getPrice());
        ebayP.put("listingtype", getListingType());
        ebayP.put("condition", getCondition());
        ebayP.put("globalid", getGlobalId());
        ebayP.put("price", getPrice());
        ebayP.put("title", getTitle());
        ebayP.put("productid", getProductId());

        return ebayP;
    }


}
