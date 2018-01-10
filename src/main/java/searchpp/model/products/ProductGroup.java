package searchpp.model.products;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.model.json.JsonObject;

import java.util.ArrayList;

/**
 * ProductGroup
 *
 * @version 1
 */
public class ProductGroup extends ArrayList<Product> implements JsonObject {

    private long productID;

    public ProductGroup(long productID) {
        this.productID = productID;
    }

    /**
     * Removed all products out of price range
     * @param min min price in cent
     * @param max max price in cent
     */
    public void setPrice(int min, int max) {
        for (int i = 0; i < size(); ++i) {
            if (get(i).getPrice() < min*0.01 || get(i).getPrice() > max*0.01) {
                remove(i);
                --i;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : this)
            stringBuilder.append(product.toString()).append('\n');
        return stringBuilder.toString();
    }

    @Override
    public JSONObject getJsonObject() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < size(); ++i)
            array.add(get(i).getJsonObject());
        JSONObject object = new JSONObject();
        object.put("data", array);
        object.put("elements", size());
        object.put("product_id", productID);

        return object;
    }
}
