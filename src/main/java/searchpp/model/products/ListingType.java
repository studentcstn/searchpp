package searchpp.model.products;

import org.json.simple.JSONObject;

/**
 * Created by Tobi on 04.12.2017.
 */
public enum ListingType
{
    //ListingType from ebay (https://developer.ebay.com/devzone/finding/callref/types/ListingInfo.html and http://developer.ebay.com/devzone/shopping/docs/callref/types/ListingTypeCodeType.html)
    ADFORMAT,   //Advertisement Product Listing
    AUCTION,    //Auction Listing
    AUCTIONWITHBIN, //Auction Listing with buy it now option
    CLASSIFIED, //Classified Advertisement Listing (sale outside ebay)
    FIXEDPRICE, //Listing with fixed price
    STOREINVENTORY,  //Listings with fixed price, that appear after other listings
    OTHER;

    public static ListingType getType(String listing)
    {
        ListingType listingType;
        switch(listing)
        {
            case "AdFormat":
            case "AdType":
                listingType = ADFORMAT;
                break;
            case "Auction":
                listingType = AUCTION;
                break;
            case "AuctionWithBIN":
                listingType = AUCTIONWITHBIN;
                break;
            case "Classified":
                listingType = CLASSIFIED;
                break;
            case "FixedPrice":
            case "FixedPriceItem":
                listingType = FIXEDPRICE;
                break;
            case "StoreInventory":
                listingType = STOREINVENTORY;
                break;
            default:
                listingType = OTHER;
                break;

        }
        return  listingType;
    }

}
