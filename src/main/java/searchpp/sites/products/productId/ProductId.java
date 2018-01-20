package searchpp.sites.products.productId;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBProduct;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.ProductGroup;
import searchpp.services.ProductSearcher;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("products/{productId}")
public class ProductId
{

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("productId") String productID, @QueryParam("price_min") int min, @QueryParam("price_max") int max, @QueryParam("used") boolean used)
    {
            //control price
            boolean price = false;
            if (min > 0 || max > 0)
                price = true;
            if (price)
            {
                if (max < 0)
                    max = 0;
                if (min < 0)
                    min = 0;

                if (min > 0 && max == 0)
                    max = Integer.MAX_VALUE;

                if (min > max)
                {
                    int tmp = min;
                    min = max;
                    max = tmp;
                }
            }

            //get asin from database
            int gId = Integer.parseInt(productID);
            String asin = DBProduct.loadAmazonProduct(gId);
            if(asin == null)
            {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }

            //get amazon product without rating
            AmazonProduct amazonProduct = ProductSearcher.searchAmazonProduct(asin, false);

            //build product group, add ebay products
            ProductGroup productGroup = new ProductGroup(gId);
            productGroup.add(amazonProduct);
            productGroup.addAll(ProductSearcher.searchEbayProductList(Long.toString(amazonProduct.getEan())));

            //control for price and used
            if (price)
                productGroup.setPrice(min, max);
            if (!used)
                productGroup.removeUsed();

            //convert to json
            JSONArray array = productGroup.getJsonList();
            JSONObject object = new JSONObject();
            object.put("product_id", productGroup.getProductID());
            object.put("elements", array.size());
            object.put("data", array);

            return object.toJSONString();
        }
    }