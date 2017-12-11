package searchpp.services;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import searchpp.model.config.Api;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.EbayProduct;
import searchpp.utils.AmazonRequestsHelper;
import searchpp.utils.ConfigLoader;
import searchpp.utils.EbayRequestsHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    private static EbayRequestsHelper _ebayRequestsHelper = new EbayRequestsHelper("svcs.ebay.com");
    private static EbayRequestsHelper _ebayRequestsHelper2 = new EbayRequestsHelper("open.api.ebay.com");

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

    public static AmazonProduct searchAmazonProduct(String productId)
    {
        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemLookup");
        params.put("IdType", "ASIN");
        params.put("ResponseGroup", "ItemAttributes, ItemIds, OfferListings, OfferSummary, Reviews, SalesRank");
        params.put("ItemId", productId);

        requestUrl = _amazonRequestsHelper.generateRequest(params, "/onca/xml");

        System.out.println(requestUrl);
        return parseAmazonRequest(requestUrl).get(0);
    }

    private static List<AmazonProduct> parseAmazonRequest(String requestUrl)
    {
        List<AmazonProduct> products = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);

            NodeList itemList = doc.getElementsByTagName("Item");

            for(int i = 0; i < itemList.getLength(); i++)
            {
                AmazonProduct product = new AmazonProduct();
                Node item = itemList.item(i);

                Element eElement = (Element) item;
                String asin = getTagValue(eElement, "ASIN");
                String title = getTagValue(eElement, "Title");
                //Todo Condition
                Element ePrice = (Element) eElement.getElementsByTagName("LowestNewPrice").item(0);
                Double price = Double.parseDouble(getTagValue(ePrice, "Amount"))/100;

                //Todo bessere Lösung wenn kein SalesRank angegeben
                String rank = getTagValue(eElement, "SalesRank");
                int salesRank  = -1;
                if(!rank.equals(""))
                    salesRank = Integer.parseInt(rank);

                /* nicht überall ist ein ean angegeben */
                String eanElement = getTagValue(eElement, "EAN");
                long ean = -1;
                if (eanElement.matches("[0-9]+"))
                    ean = Long.parseLong(eanElement);
                String manufacturer = getTagValue(eElement, "Manufacturer");
                String model = getTagValue(eElement, "Model");
                //Todo Rating

                product.setProductId(asin);
                product.setTitle(title);
                //Todo product.setCondition();
                product.setPrice(price);
                product.setEan(ean);
                product.setManufacturer(manufacturer);
                product.setModel(model);
                product.setSalesRank(salesRank);
                //Todo product.setRating();

                products.add(product);

                System.out.println("ASIN: " +asin);
                System.out.println("EAN: " + ean);
                System.out.println("SalesRank: " + salesRank);
                System.out.println("Manufacturer: " + manufacturer);
                System.out.println("Model: " + model);
                System.out.println("Title: " + title);
                System.out.println("Price: " + price);
                System.out.println("------------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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

        requestUrl = _ebayRequestsHelper.generateRequest(params, "/services/search/FindingService/v1");

        System.out.println(requestUrl);
        return parseEbayRequest(requestUrl);
    }

    public static EbayProduct searchEbayProduct(String productId)
    {
        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("appid" , ConfigLoader.getConfig("ebay", Api.clientID));
        params.put("callname" , "GetSingleItem");
        params.put("responseencoding", "XML");
        params.put("siteid", "77");
        params.put("version", "967");
        params.put("ItemID", productId);

        requestUrl = _ebayRequestsHelper2.generateRequest(params, "/shopping");

        System.out.println(requestUrl);
        //return parseEbayRequest(requestUrl);
        return null;
    }

    private static List<EbayProduct> parseEbayRequest(String requestUrl)
    {
        List<EbayProduct> products = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);

            NodeList itemList = doc.getElementsByTagName("item");

            for(int i = 0; i < itemList.getLength(); i++)
            {
                EbayProduct product = new EbayProduct();
                Node item = itemList.item(i);

                Element eElement = (Element) item;
                String itemId = getTagValue(eElement, "itemId");
                String title = getTagValue(eElement, "title");
                //Todo Condition
                Element ePrice = (Element) eElement.getElementsByTagName("sellingStatus").item(0);
                Double price = Double.parseDouble(getTagValue(ePrice, "currentPrice"));
                //Todo ListingType

                product.setProductId(itemId);
                product.setTitle(title);
                //Todo product.setCondition();
                product.setPrice(price);
                //Todo product.setListingType();

                products.add(product);

                System.out.println("ItemId: " + itemId);
                System.out.println("Title: " + title);
                System.out.println("Price: " + price);
                System.out.println("------------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    private static String getTagValue(Element eElement, String tag)
    {
        String ret = "";
        if(eElement.getElementsByTagName(tag).item(0) != null)
            ret = eElement.getElementsByTagName(tag).item(0).getTextContent();
        return ret;
    }
}
