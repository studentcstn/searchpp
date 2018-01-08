package searchpp.sites.products;

import com.google.api.client.json.Json;
import com.mysql.cj.xdevapi.JsonArray;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.EbayProduct;
import searchpp.services.ConverterToJson;
import searchpp.services.ProductSearcher;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Path("products")
public class Products {

    @GET
   // @Produces(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    //public String get(String products) { return "products get"; }
    public String get (){

        String products="Raspberry 3";
        //System.out.println(products);

        List<EbayProduct> ebayProducts = ProductSearcher.searchEbayProductList(products);
        List<AmazonProduct> amazonProducts = ProductSearcher.searchAmazonProductList(products);

        System.out.println(products+"List");

        ConverterToJson converter=new ConverterToJson();
        for(EbayProduct item: ebayProducts){
            converter.addJsonList(item.getJsonItem());
        }
        for(AmazonProduct item: amazonProducts){
            converter.addJsonList(item.getJsonItem());
        }
        System.out.println("Erfolgreich Convertiert");
        return converter.getJsonList().toString();
    }
}