package searchpp.model.products;

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

    public EbayProduct(String productId)
    {}

    public void setListingType(ListingType listingType) {
        _listingType = listingType;
    }
    public ListingType get_listingType() {
        return _listingType;
    }
}
