package searchpp.services;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import searchpp.model.config.Api;
import searchpp.model.products.*;
import searchpp.utils.AmazonRequestsHelper;
import searchpp.utils.ConfigLoader;
import searchpp.utils.EbayRequestsHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tobi on 08.12.2017.
 */
public class ProductSearcher
{
    /**
     * The request helper for amazon
     */
    private static AmazonRequestsHelper _amazonRequestsHelper = new AmazonRequestsHelper("ecs.amazonaws.de");
    /**
     * The request helper for ebay (for multiple products)
     */
    private static EbayRequestsHelper _ebayRequestsHelperSvcs = new EbayRequestsHelper("svcs.ebay.com");
    /**
     * The request helper for ebay (for single product)
     */
    private static EbayRequestsHelper _ebayRequestsHelperOpenApi = new EbayRequestsHelper("open.api.ebay.com");

    /**
     * Searches for multiple products, that refer to the searchString on amazon
     * @param searchString The string for what you are looking for
     * @param rating Defines if a rating should be retrieved from amazon
     * @return A list of Amazon Products
     */
    public static List<AmazonProduct> searchAmazonProductList(String searchString, boolean rating)
    {
        Map<String, String> params = new HashMap<>();
        params.put("Keywords", searchString);

        return searchAmazon(params, rating);
    }

    /**
     * Searches for multiple products, that refer to the searchString with a minimum and maximum price on amazon
     * @param searchString The string for what you are looking for
     * @param rating Defines if a rating should be retrieved from amazon
     * @param minPrice The minimum price of the products in euro
     * @param maxPrice The maximum price of the products in euro
     * @return A list of AmazonProducts within minimum and maximum price
     */
    public static List<AmazonProduct> searchAmazonProductList(String searchString, boolean rating, double minPrice, double maxPrice)
    {
        Map<String, String> params = new HashMap<>();
        params.put("Keywords", searchString);

        if(minPrice > maxPrice)
        {
            double tmp = minPrice;
            minPrice = maxPrice;
            maxPrice = tmp;
        }

        params.put("MinimumPrice", Integer.toString((int)minPrice*100));
        params.put("MaximumPrice", Integer.toString((int)maxPrice*100));

        return searchAmazon(params, rating);
    }

    /**
     * Searches for a single product on amazon with the amazon ASIN
     * @param amazonASIN The ID of a specified amazon product
     * @param rating Defines if a rating should be retrieved from amazon
     * @return A single AmazonProduct
     */
    public static AmazonProduct searchAmazonProduct(String amazonASIN, boolean rating)
    {
        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemLookup");
        params.put("IdType", "ASIN");
        params.put("ResponseGroup", "Images, ItemAttributes, ItemIds, OfferListings, OfferSummary, Reviews, SalesRank");
        params.put("ItemId", amazonASIN);

        //Get the signed url for the request
        requestUrl = _amazonRequestsHelper.generateRequest(params, "/onca/xml");

        System.out.println(requestUrl);
        List<AmazonProduct> products = parseAmazonRequest(requestUrl, rating);
        if(products.size() < 1)
            return null;
        else
            return products.get(0);
    }

    /**
     * Internal method to get multiple AmazonProducts
     * @param params The map with the parameter of a request
     * @param rating Defines if a rating should be retrieved from amazon
     * @return A list of AmazonProducts
     */
    private static List<AmazonProduct> searchAmazon(Map<String, String> params, boolean rating)
    {
        String requestUrl;

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "All");
        params.put("ResponseGroup", "Images, ItemAttributes, ItemIds, OfferListings, OfferSummary, Reviews, SalesRank");

        List<AmazonProduct> products = new ArrayList<>();

        //Generate multiple request (amazon returns 10 products per page and you can request 5 pages)
        for(int i = 1; i <= 5; i++)
        {
            params.put("ItemPage", Integer.toString(i));
            requestUrl = _amazonRequestsHelper.generateRequest(params, "/onca/xml");
            System.out.println(requestUrl);
            products.addAll(parseAmazonRequest(requestUrl, rating));
        }

        return products;
    }

    /**
     * Internal method that parses the XML document that comes from amazon
     * @param requestUrl The Url of the amazon request
     * @param rating Defines if a rating should be retrieved from amazon
     * @return A list of AmazonProducts
     */
    private static List<AmazonProduct> parseAmazonRequest(String requestUrl, boolean rating)
    {
        long time = 0;
        List<AmazonProduct> products = new ArrayList<>();

        //Generate up to 5 request if amazon returns 503 RequestThrottled - You are submitting requests too quickly. Please retry your requests at a slower rate.
        for(int n = 0; n < 5; n++)
        {
            try
            {
                //Parse the request
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(requestUrl);

                NodeList itemList = doc.getElementsByTagName("Item");

                //get all the items in the answer
                for (int i = 0; i < itemList.getLength(); i++)
                {
                    AmazonProduct product = new AmazonProduct();
                    Node item = itemList.item(i);

                    Element eElement = (Element) item;

                    //ignore Kindle eBooks (many required tags are missing)
                    if(getTagValue(eElement, "Format").equals("Kindle eBook"))
                        continue;

                    //get the ASIN
                    String asin = getTagValue(eElement, "ASIN");

                    //get the title
                    String title = getTagValue(eElement, "Title");

                    Element eCondtion = (Element) eElement.getElementsByTagName("OfferAttributes").item(0);
                    if(eCondtion == null)
                        continue;

                    //get the condition
                    Condition condition = Condition.getProductCondition(getTagValue(eCondtion, "Condition"));

                    Element ePrice = (Element) eElement.getElementsByTagName("LowestNewPrice").item(0);
                    if(ePrice == null)
                        continue;

                    //get the price
                    String sPrice = getTagValue(ePrice, "Amount");
                    double price = 0;
                    if(!sPrice.equals(""))
                        price = Double.parseDouble(sPrice) / 100;

                    //get the sales rank
                    String rank = getTagValue(eElement, "SalesRank");
                    int salesRank = -1;
                    if (!rank.equals(""))
                        salesRank = Integer.parseInt(rank);

                    //get the ean
                    String eanElement = getTagValue(eElement, "EAN");
                    long ean = -1;
                    if (eanElement.matches("[0-9]+"))
                        ean = Long.parseLong(eanElement);

                    //get the manufacturer
                    String manufacturer = getTagValue(eElement, "Manufacturer");

                    //get the model
                    String model = getTagValue(eElement, "Model");

                    Element eImage = (Element) eElement.getElementsByTagName("LargeImage").item(0);
                    if(eImage == null)
                        continue;

                    //get the image url
                    String imgUrl = getTagValue(eImage, "URL");

                    //get the webpage url
                    String productUrl = getTagValue(eElement, "DetailPageURL");

                    //check if product has amazon ratings
                    String hasReviews = getTagValue(eElement, "HasReviews");
                    boolean reviews = false;
                    if (hasReviews.matches("true|false"))
                        reviews = Boolean.parseBoolean(hasReviews);

                    //skip all products that match the if condition
                    if(asin.equals("") || title.equals("") || price == 0 || condition == null || ean == -1 || !reviews)
                        continue;

                    //set the attributes
                    product.setProductId(asin);
                    product.setTitle(title);
                    product.setCondition(condition);
                    product.setPrice(price);
                    product.setEan(ean);
                    product.setManufacturer(manufacturer);
                    product.setModel(model);
                    product.setSalesRank(salesRank);
                    product.setImgUrl(imgUrl);
                    product.setProductUrl(productUrl);

                    //if defined get the rating
                    if (rating) {
                        AmazonProductRating amazonProductRating = AmazonRating.getRating(product);
                        product.setRating(amazonProductRating);
                    }

                    //add the products to the list
                    products.add(product);

                    //for debugging
                    System.out.println("ASIN: " + product.getProductId());
                    System.out.println("EAN: " + product.getEan());
                    System.out.println("SalesRank: " + product.getSalesRank());
                    System.out.println("Manufacturer: " + product.getManufacturer());
                    System.out.println("Model: " + product.getModel());
                    System.out.println("Title: " + product.getTitle());
                    System.out.println("Condition: " + product.getCondition());
                    System.out.println("Price: " + product.getPrice());
                    System.out.println("Img: " + product.getImgUrl());
                    System.out.println("Url: " + product.getProductUrl());
                    if (product.getRating() != null)
                        System.out.println("Rating: " + product.getRating().toString());
                    System.out.println("------------");
                }

                return products;
            } catch (ParserConfigurationException e)
            {
                System.err.println("ParserConfigurationException");
            } catch (IOException e)
            {
                //amazon returned 503 try again in some time
                System.err.println("Error during request: " + e.getMessage());
                //long time = (long) (Math.random() * 5000 + 5000);
                time += 1000;
                System.err.println("Try again in " + time + " ms");
                try
                {
                    Thread.sleep(time);
                } catch (InterruptedException e1)
                {}

            } catch (SAXException e)
            {
                System.err.println("SAXException");
            }
        }

        return products;
    }

    /**
     * Searches for multiple products, that refer to the searchString on ebay
     * @param searchString The string for what you are looking for
     * @return A list of EbayProducts
     */
    public static List<EbayProduct> searchEbayProductList(String searchString)
    {
        Map<String, String> params = new HashMap<>();

        params.put("keywords", searchString);

        return searchEbay(params);
    }

    /**
     * Searches for multiple products, that refer to the searchString with a minimum and maximum price on ebay
     * @param searchString The string for what you are looking for
     * @param minPrice The minimum price of the products in euro
     * @param maxPrice The maximum price of the products in euro
     * @return A list of EbayProducts within minimum and maximum price
     */
    public static List<EbayProduct> searchEbayProductList(String searchString, double minPrice, double maxPrice)
    {
        Map<String, String> params = new HashMap<>();

        params.put("keywords", searchString);

        if(minPrice > maxPrice)
        {
            double tmp = minPrice;
            minPrice = maxPrice;
            maxPrice = tmp;
        }

        params.put("itemFilter(0).name", "MinPrice");
        params.put("itemFilter(0).value", Double.toString(minPrice));
        params.put("itemFilter(1).name", "MaxPrice");
        params.put("itemFilter(1).value", Double.toString(maxPrice));

        return searchEbay(params);
    }

    /**
     * Internal method to get multiple EbayProducts
     * @param params The map with the parameter of a request
     * @return A list of EbayProducts
     */
    private static List<EbayProduct> searchEbay(Map<String, String> params)
    {
        String requestUrl;

        params.put("SECURITY-APPNAME", ConfigLoader.getConfig("ebay", Api.clientID));
        params.put("OPERATION-NAME" , "findItemsAdvanced");
        params.put("SERVICE-VERSION", "1.0.0");
        params.put("RESPONSE-DATA-FORMAT", "XML");
        params.put("REST-PAYLOAD", "true");
        params.put("GLOBAL-ID", "EBAY-DE");

        requestUrl = _ebayRequestsHelperSvcs.generateRequest(params, "/services/search/FindingService/v1");

        System.out.println(requestUrl);
        return parseEbayProductList(requestUrl);
    }

    /**
     * Internal method that parses the XML document that comes from ebay
     * @param requestUrl The Url of the ebay request
     * @return A list of EbayProducts
     */
    private static List<EbayProduct> parseEbayProductList(String requestUrl)
    {
        List<EbayProduct> products = new ArrayList<>();

        //if request fails (shouldn't happen)
        for(int n = 0; n < 5; n++)
        {
            try
            {
                //same as parseAmazonRequest with other tags
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(requestUrl);

                NodeList itemList = doc.getElementsByTagName("item");

                for (int i = 0; i < itemList.getLength(); i++)
                {
                    EbayProduct product = new EbayProduct();
                    Node item = itemList.item(i);

                    Element eElement = (Element) item;
                    String itemId = getTagValue(eElement, "itemId");
                    String title = getTagValue(eElement, "title");

                    Element eCondition = (Element) eElement.getElementsByTagName("condition").item(0);
                    Condition condition = Condition.getProductCondition(getTagValue(eCondition, "conditionId"));

                    Element ePrice = (Element) eElement.getElementsByTagName("sellingStatus").item(0);
                    String sPrice = getTagValue(ePrice, "currentPrice");
                    double price = 0;
                    if (!sPrice.equals(""))
                        price = Double.parseDouble(sPrice);

                    Element eListingType = (Element) eElement.getElementsByTagName("listingInfo").item(0);
                    ListingType listingType = ListingType.getType(getTagValue(eListingType, "listingType"));

                    String imgUrl = getTagValue(eElement, "galleryURL");

                    String productUrl = getTagValue(eElement, "viewItemURL");

                    if (itemId.equals("") || title.equals("") || price == 0)
                        continue;

                    product.setProductId(itemId);
                    product.setTitle(title);
                    product.setCondition(condition);
                    product.setPrice(price);
                    product.setListingType(listingType);
                    product.setImgUrl(imgUrl);
                    product.setProductUrl(productUrl);

                    products.add(product);

                    //for debugging
                    System.out.println("ItemId: " + product.getProductId());
                    System.out.println("Title: " + product.getTitle());
                    System.out.println("Price: " + product.getPrice());
                    System.out.println("Condition: " + product.getCondition());
                    System.out.println("ListingType: " + product.getListingType());
                    System.out.println("Img: " + product.getImgUrl());
                    System.out.println("Url: " + product.getProductUrl());
                    System.out.println("------------");
                }

                return products;
            } catch (ParserConfigurationException e)
            {
                System.err.println("ParserConfigurationException");
            } catch (IOException e)
            {
                System.err.println("Error during request, trying again...");
            } catch (SAXException e)
            {
                System.err.println("SAXException");
            }
        }
        return products;
    }

    /**
     * Searches for a single product on ebay with the ebay ID
     * @param product The EbayProduct with the ID of ebay set
     * @return A single EbayProduct
     */
    public static EbayProduct searchEbayProduct(EbayProduct product)
    {
        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("appid" , ConfigLoader.getConfig("ebay", Api.clientID));
        params.put("callname" , "GetSingleItem");
        params.put("responseencoding", "XML");
        params.put("siteid", "77");
        params.put("version", "967");
        params.put("ItemID", product.getProductId());

        requestUrl = _ebayRequestsHelperOpenApi.generateRequest(params, "/shopping");

        System.out.println(requestUrl);
        return parseEbayProduct(requestUrl);
    }

    /**
     * Internal method that parses the XML document that comes from ebay
     * @param requestUrl The Url of the ebay request
     * @return A single EbayProduct
     */
    private static EbayProduct parseEbayProduct(String requestUrl)
    {
        EbayProduct product = new EbayProduct();

        for(int n = 0; n < 5; n++)
        {
            try
            {
                //Same as parseAmazonRequest with other tags
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(requestUrl);

                NodeList itemList = doc.getElementsByTagName("Item");

                for (int i = 0; i < itemList.getLength(); i++)
                {
                    Node item = itemList.item(i);

                    Element eElement = (Element) item;
                    String itemId = getTagValue(eElement, "ItemID");
                    String title = getTagValue(eElement, "Title");

                    Condition condition = Condition.getProductCondition(getTagValue(eElement, "ConditionID"));

                    String sPrice = getTagValue(eElement, "ConvertedCurrentPrice");
                    double price = 0;
                    if (!sPrice.equals(""))
                        price = Double.parseDouble(sPrice);

                    ListingType listingType = ListingType.getType(getTagValue(eElement, "ListingType"));

                    String imgUrl = getTagValue(eElement, "PictureURL");

                    String productUrl = getTagValue(eElement, "ViewItemURLForNaturalSearch");

                    if (itemId.equals("") || title.equals("") || price == 0)
                        continue;

                    product.setProductId(itemId);
                    product.setTitle(title);
                    product.setCondition(condition);
                    product.setPrice(price);
                    product.setListingType(listingType);
                    product.setImgUrl(imgUrl);
                    product.setProductUrl(productUrl);

                    //for debugging
                    System.out.println("ItemId: " + product.getProductId());
                    System.out.println("Title: " + product.getTitle());
                    System.out.println("Price: " + product.getPrice());
                    System.out.println("Condition: " + product.getCondition());
                    System.out.println("ListingType: " + product.getListingType());
                    System.out.println("Img: " + product.getImgUrl());
                    System.out.println("Url: " + product.getProductUrl());
                    System.out.println("------------");
                }

                return product;

            } catch (ParserConfigurationException e)
            {
                System.err.println("ParserConfigurationException");
            } catch (IOException e)
            {
                System.err.println("Error during request, trying again...");
            } catch (SAXException e)
            {
                System.err.println("SAXException");
            }
        }
        return null;
    }

    /**
     * Internal method to get the value of a tag in an element
     * @param eElement The element with the tag/value pair
     * @param tag The tag to search for
     * @return The value of the tag
     */
    private static String getTagValue(Element eElement, String tag)
    {
        String ret = "";
        if(eElement.getElementsByTagName(tag).item(0) != null)
            ret = eElement.getElementsByTagName(tag).item(0).getTextContent();
        return ret;
    }
}
