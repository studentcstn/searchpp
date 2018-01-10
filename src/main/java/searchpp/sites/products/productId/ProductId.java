package searchpp.sites.products.productId;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.ProductGroup;
import searchpp.services.ProductSearcher;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("products/{productId}")
public class ProductId {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("productId") String productID) {

        String asin;

        //todo get asin with productID from database

        //todo remove next line
        asin = "B01CD5VC92";

        AmazonProduct amazonProduct = ProductSearcher.searchAmazonProduct(asin);

        ProductGroup  productGroup = new ProductGroup(amazonProduct.getGlobalId());
        productGroup.add(amazonProduct);
        productGroup.addAll(ProductSearcher.searchEbayProductList(Long.toString(amazonProduct.getEan())));

        JSONArray array = productGroup.getJsonList();
        JSONObject object = new JSONObject();
        object.put("product_id", productGroup.getProductID());
        object.put("elements", array.size());
        object.put("data", array);

        return object.toJSONString();
    }
}