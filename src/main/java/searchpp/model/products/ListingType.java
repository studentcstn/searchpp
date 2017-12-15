package searchpp.model.products;

/**
 * Created by Tobi on 04.12.2017.
 */
public enum ListingType
{
    //ListingType from ebay (https://developer.ebay.com/devzone/finding/callref/types/ListingInfo.html)
    ADFORMAT,   //Advertisement Product Listing
    AUCTION,    //Auction Listing
    AUCTIONWITHBIN, //Auction Listing with buy it now option
    CLASSIFIED, //Classified Advertisement Listing (sale outside ebay)
    FIXEDPRICE, //Listing with fixed price
    STOREINVENTORY  //Listings with fixed price, that appear after other listings

}
