package searchpp.model.products;

import com.mysql.cj.xdevapi.JsonArray;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBProduct;
import searchpp.model.json.JsonList;
import searchpp.model.json.JsonObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ProductGroup
 *
 * @version 1
 */
public class ProductGroup extends ArrayList<Product> implements JsonObject, JsonList {

    private int productID;

    private boolean price = false;
    private double priceMin = Double.MAX_VALUE;
    private double priceMax = Double.MIN_VALUE;

    private boolean _new = false;
    private boolean used = false;

    public ProductGroup(){}
    public ProductGroup(int gId)
    {
        this.productID = gId;
    }

    public int getProductID() {
        return productID;
    }

    @Override
    public boolean add(Product product) {
        boolean add = super.add(product);

        if (!add)
            return false;

        setProduct(product);

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Product> c) {
        boolean add = super.addAll(c);

        if (!add)
            return false;

        for (Product product : c)
            setProduct(product);

        return true;
    }

    public boolean saveToDatabase()
    {
        return DBProduct.saveProducts(this);
    }

    private void setProduct(Product product) {
        if (product.getPrice() < priceMin) {
            priceMin = product.getPrice();
            price = true;
        }
        if (product.getPrice() > priceMax) {
            priceMax = product.getPrice();
            price = true;
        }

        if (product.getCondition() == Condition.NEW)
            _new = true;
        else
            used = true;
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
        JSONObject object = new JSONObject();
        object.put("product_id", productID);

        if (price) {
            object.put("price_min", priceMin);
            object.put("price_max", priceMax);
        }

        if (size() > 0) {
            object.put("name", get(0).getTitle());
            for (int i = 0; i < size(); ++i) {
                if (get(i).getClass() == AmazonProduct.class) {
                    object.put("rating", ((AmazonProduct) get(i)).getRating().getAverageRating());
                    break;
                }
            }
            object.put("img", get(0).getImgUrl());
        }
        JSONArray array = new JSONArray();
        if (_new)
            array.add("NEW");
        if (used)
            array.add("USED");

        object.put("types", array);

        return object;
    }

    @Override
    public JSONArray getJsonList() {
        JSONArray array = new JSONArray();
        for (Product product : this)
            array.add(product.getJsonObject());

        return array;
    }

    public void setGlobalId(int globalId)
    {
        this.productID = globalId;
    }
}
