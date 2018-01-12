package searchpp.sites.products.productId;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBProduct;
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

        int gId = Integer.parseInt(productID);
        String asin = DBProduct.loadAmazonProduct(gId);

        AmazonProduct amazonProduct = ProductSearcher.searchAmazonProduct(asin);

        ProductGroup  productGroup = new ProductGroup(gId);
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