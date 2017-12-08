package searchpp.services;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.Condition;
import searchpp.model.products.EbayProduct;
import searchpp.utils.AmazonSignedRequestsHelper;

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
    public static List<AmazonProduct> searchAmazonProduct(String searchString)
    {
        AmazonSignedRequestsHelper helper;
        try
        {
            helper = AmazonSignedRequestsHelper.getInstance("ecs.amazonaws.de", "!!!!!AWS_ACCESS_KEY_ID!!!!!", "!!!!!AWS_SECRET_KEY!!!!!");
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        String requestUrl;

        Map<String, String> params = new HashMap<>();
        params.put("Service", "AWSECommerceService");
        params.put("AssociateTag" , "!!!!!AssocTag!!!!!");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "All");
        params.put("ResponseGroup", "ItemAttributes, ItemIds, OfferListings, OfferSummary, Reviews, SalesRank");
        params.put("Keywords", searchString);

        requestUrl = helper.sign(params);


        return parseRequest(requestUrl);
    }

    private static List<AmazonProduct> parseRequest(String requestUrl)
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
                int salesRank  = 99999;
                if(!rank.equals(""))
                    salesRank = Integer.parseInt(rank);

                int ean = Integer.parseInt(getTagValue(eElement, "EAN"));
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

                /*System.out.println("ASIN: " +asin);
                System.out.println("EAN: " + ean);
                System.out.println("SalesRank: " + salesRank);
                System.out.println("Manufacturer: " + manufacturer);
                System.out.println("Model: " + model);
                System.out.println("Title: " + title);
                System.out.println("Price: " + price);
                System.out.println("------------");*/
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


    public static List<EbayProduct> searchEbayProduct(String searchString)
    {
        List<EbayProduct> products = new ArrayList<>();

        return products;
    }
}
