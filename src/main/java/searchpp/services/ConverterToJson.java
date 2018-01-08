package searchpp.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ConverterToJson {

    //private JSONObject productlist;

    private JSONArray productlist;

    public ConverterToJson(){
        productlist= new JSONArray();
    }
   /* public JSONObject product(AmazonProduct amazonProduct){
        JSONObject amazonP = new JSONObject();
        amazonP.put("ean", amazonProduct.getEan());
        amazonP.put("manufacturer", amazonProduct.getManufacturer());
        amazonP.put("model", amazonProduct.getModel());
        amazonP.put("reating",amazonProduct.getRating());
        amazonP.put("salesrank",amazonProduct.getSalesRank());

        return  amazonP;
    }
    public JSONObject product(EbayProduct ebayProduct){
        JSONObject ebayP = new JSONObject();
        ebayP.put("listingtype", ebayProduct.getListingType());
        ebayP.put("condition",ebayProduct.getCondition());
        ebayP.put("globalid",ebayProduct.getGlobalId());
        ebayP.put("price",ebayProduct.getPrice());
        ebayP.put("title",ebayProduct.getTitle());
        ebayP.put("productid",ebayProduct.getProductId());

        return ebayP;
    } */

    public void addJsonList(JSONObject productElement){
        //productlist.put("productlist", productElement);
        productlist.add(productElement);
    }
    public JSONArray getJsonList(){return productlist;}

    public void clearJsonList(){ productlist.clear(); }

    public JSONArray getJson(){ return productlist; }
}
