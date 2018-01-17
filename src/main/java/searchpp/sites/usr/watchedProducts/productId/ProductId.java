package searchpp.sites.usr.watchedProducts.productId;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBProduct;
import searchpp.database.DBUser;
import searchpp.model.products.PriceHistory;
import searchpp.model.products.ProductGroup;
import searchpp.model.user.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("usr/{userToken}/watchedProducts/{productId}")
public class ProductId {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("userToken") String userToken, @PathParam("productId") int productId) {
        User user = DBUser.loadUserByToken(userToken);
        JSONArray array = new JSONArray();
        if (user != null)
        {
            List<PriceHistory> priceHistoryList = DBProduct.loadPriceHistory(productId);
            for (PriceHistory priceHist : priceHistoryList) {
                array.add(priceHist.getJsonObject());
            }
        }
        JSONObject object = new JSONObject();
        object.put("data", array);
        object.put("elements", array.size());

        return object.toJSONString();
    }

    @PUT
    public void put(@PathParam("userToken") String userToken, @PathParam("productId") int productId, @QueryParam("date_to") Date date_to, @QueryParam("date_from") Date date_from) {
        User user = DBUser.loadUserByToken(userToken);
        if (user != null)
        {
            DBUser.changeWatchedProduct(user, productId, date_from, date_to);
        }
    }

    @DELETE
    public void delete(@PathParam("userToken") String userToken, @PathParam("productId") int productId) {
        User user = DBUser.loadUserByToken(userToken);
        if (user != null)
        {
            DBUser.removeWatchedProduct(user, productId);
        }
    }
}