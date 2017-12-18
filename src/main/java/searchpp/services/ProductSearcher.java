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
    private static AmazonRequestsHelper _amazonRequestsHelper = new AmazonRequestsHelper("ecs.amazonaws.de");
    private static EbayRequestsHelper _ebayRequestsHelperSvcs = new EbayRequestsHelper("svcs.ebay.com");
    private static EbayRequestsHelper _ebayRequestsHelperOpenApi = new EbayRequestsHelper("open.api.ebay.com");

    public static List<AmazonProduct> searchAmazonProductList(String searchString)
    {
        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "All");
        params.put("ResponseGroup", "ItemAttributes, ItemIds, OfferListings, OfferSummary, Reviews, SalesRank");
        params.put("Keywords", searchString);

        requestUrl = _amazonRequestsHelper.generateRequest(params, "/onca/xml");

        System.out.println(requestUrl);
        return parseAmazonRequest(requestUrl);
    }

    public static AmazonProduct searchAmazonProduct(AmazonProduct product)
    {
        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemLookup");
        params.put("IdType", "ASIN");
        params.put("ResponseGroup", "ItemAttributes, ItemIds, OfferListings, OfferSummary, Reviews, SalesRank");
        params.put("ItemId", product.getProductId());

        requestUrl = _amazonRequestsHelper.generateRequest(params, "/onca/xml");

        System.out.println(requestUrl);
        List<AmazonProduct> products = parseAmazonRequest(requestUrl);
        if(products.size() < 1)
            return null;
        else
            return products.get(0);
    }

    private static List<AmazonProduct> parseAmazonRequest(String requestUrl)
    {
        List<AmazonProduct> products = new ArrayList<>();

        for(int n = 0; n < 5; n++)
        {
            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(requestUrl);

                NodeList itemList = doc.getElementsByTagName("Item");

                for (int i = 0; i < itemList.getLength(); i++)
                {
                    AmazonProduct product = new AmazonProduct();
                    Node item = itemList.item(i);

                    Element eElement = (Element) item;

                    String asin = getTagValue(eElement, "ASIN");

                    String title = getTagValue(eElement, "Title");

                    Element eCondtion = (Element) eElement.getElementsByTagName("OfferAttributes").item(0);
                    Condition condition = Condition.getProductCondition(getTagValue(eCondtion, "Condition"));

                    Element ePrice = (Element) eElement.getElementsByTagName("LowestNewPrice").item(0);
                    String sPrice = getTagValue(ePrice, "Amount");
                    double price = 0;
                    if(!sPrice.equals(""))
                        price = Double.parseDouble(sPrice) / 100;

                    String rank = getTagValue(eElement, "SalesRank");
                    int salesRank = -1;
                    if (!rank.equals(""))
                        salesRank = Integer.parseInt(rank);

                    String eanElement = getTagValue(eElement, "EAN");
                    long ean = -1;
                    if (eanElement.matches("[0-9]+"))
                        ean = Long.parseLong(eanElement);
                    String manufacturer = getTagValue(eElement, "Manufacturer");
                    String model = getTagValue(eElement, "Model");

                    if(asin.equals("") || title.equals("") || price == 0)
                        break;

                    product.setProductId(asin);
                    product.setTitle(title);
                    product.setCondition(condition);
                    product.setPrice(price);
                    product.setEan(ean);
                    product.setManufacturer(manufacturer);
                    product.setModel(model);
                    product.setSalesRank(salesRank);

                    AmazonProductRating amazonProductRating = AmazonRating.getRating(product);
                    product.setRating(amazonProductRating);

                    products.add(product);

                    System.out.println("ASIN: " + product.getProductId());
                    System.out.println("EAN: " + product.getEan());
                    System.out.println("SalesRank: " + product.getSalesRank());
                    System.out.println("Manufacturer: " + product.getManufacturer());
                    System.out.println("Model: " + product.getModel());
                    System.out.println("Title: " + product.getTitle());
                    System.out.println("Condition: " + product.getCondition());
                    System.out.println("Price: " + product.getPrice());
                    System.out.println("Rating: " + product.getRating().toString());
                    System.out.println("------------");
                }

                return products;
            } catch (Exception e)
            {
                System.err.println("Error during request, trying again...");
            }
        }

        return products;
    }

    public static List<EbayProduct> searchEbayProductList(String searchString)
    {
        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("SECURITY-APPNAME", ConfigLoader.getConfig("ebay", Api.clientID));
        params.put("OPERATION-NAME" , "findItemsAdvanced");
        params.put("SERVICE-VERSION", "1.0.0");
        params.put("RESPONSE-DATA-FORMAT", "XML");
        params.put("REST-PAYLOAD", "true");
        params.put("GLOBAL-ID", "EBAY-DE");
        params.put("keywords", searchString);

        requestUrl = _ebayRequestsHelperSvcs.generateRequest(params, "/services/search/FindingService/v1");

        System.out.println(requestUrl);
        return parseEbayProductList(requestUrl);
    }

    private static List<EbayProduct> parseEbayProductList(String requestUrl)
    {
        List<EbayProduct> products = new ArrayList<>();

        for(int n = 0; n < 5; n++)
        {
            try
            {
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

                    if (itemId.equals("") || title.equals("") || price == 0)
                        break;

                    product.setProductId(itemId);
                    product.setTitle(title);
                    product.setCondition(condition);
                    product.setPrice(price);
                    product.setListingType(listingType);

                    products.add(product);

                    System.out.println("ItemId: " + product.getProductId());
                    System.out.println("Title: " + product.getTitle());
                    System.out.println("Price: " + product.getPrice());
                    System.out.println("Condition: " + product.getCondition());
                    System.out.println("ListingType: " + product.getListingType());
                    System.out.println("------------");
                }

                return products;
            } catch (Exception e)
            {
                System.err.println("Error while requesting, trying again...");
            }
        }
        return products;
    }

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

    private static EbayProduct parseEbayProduct(String requestUrl)
    {
        EbayProduct product = new EbayProduct();

        for(int n = 0; n < 5; n++)
        {
            try
            {
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

                    if (itemId.equals("") || title.equals("") || price == 0)
                        break;

                    product.setProductId(itemId);
                    product.setTitle(title);
                    product.setCondition(condition);
                    product.setPrice(price);
                    product.setListingType(listingType);

                    System.out.println("ItemId: " + product.getProductId());
                    System.out.println("Title: " + product.getTitle());
                    System.out.println("Price: " + product.getPrice());
                    System.out.println("Condition: " + product.getCondition());
                    System.out.println("ListingType: " + product.getListingType());
                    System.out.println("------------");
                }

                return product;

            } catch (Exception e)
            {
                System.err.println("Error while requesting, trying again...");
            }
        }
        return null;
    }


    private static String getTagValue(Element eElement, String tag)
    {
        String ret = "";
        if(eElement.getElementsByTagName(tag).item(0) != null)
            ret = eElement.getElementsByTagName(tag).item(0).getTextContent();
        return ret;
    }
}
