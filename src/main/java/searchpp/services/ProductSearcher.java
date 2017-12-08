package searchpp.services;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import searchpp.model.products.AmazonProduct;
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
    static List<AmazonProduct> searchAmazonProduct(String searchString)
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

        String requestUrl = null;

        Map<String, String> params = new HashMap<String, String>();
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
            //TODO Irgendwie parsen....
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return products;
    }


    static List<EbayProduct> searchEbayProduct(String searchString)
    {
        List<EbayProduct> products = new ArrayList<>();

        return products;
    }
}
