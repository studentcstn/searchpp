package searchpp.model.products;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.database.DBProduct;
import searchpp.model.json.JsonList;
import searchpp.model.json.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * ProductGroup
 *
 * A collection of Amazon and eBay products
 */
public class ProductGroup extends ArrayList<Product> implements JsonObject, JsonList {

    private int productID;

    private boolean price = false;
    private double priceMin = Double.MAX_VALUE;
    private double priceMax = Double.MIN_VALUE;

    private boolean _new = false;
    private boolean used = false;

    private String title = null;
    private String img = null;

    private Date _from;
    private Date _to;

    private double averageRating = Double.NaN;

    public ProductGroup(){}
    public ProductGroup(int gId)
    {
        this.productID = gId;
    }

    public int getProductID() {
        return productID;
    }

    /**
     * Use to store the from date for watched products
     */
    public void setDateFrom(Date from)
    {
        _from = from;
    }

    /**
     * Use to store the to date for watched products
     */
    public void setDateTo(Date to)
    {
        _to = to;
    }

    /**
     * Add products to this group
     * Amazon products are used for information about the group
     */
    @Override
    public boolean add(Product product) {
        boolean add = super.add(product);

        if (!add)
            return false;

        if (product.getClass() == AmazonProduct.class)
            setData((AmazonProduct) product);

        useProduct(product);

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Product> c) {
        boolean add = super.addAll(c);

        if (!add)
            return false;

        for (Product product : c) {
            if (product.getClass() == AmazonProduct.class)
                setData((AmazonProduct) product);
            useProduct(product);
        }

        return true;
    }

    private void setData(AmazonProduct product) {
        title = product.getTitle();
        img = product.getImgUrl();
        if (product.getRating() != null)
            averageRating = product.getRating().getAverageRating();
    }

    public boolean saveToDatabase()
    {
        return DBProduct.saveProducts(this);
    }

    /**
     * If a product is used in this group, the minimum and maximum prices will be calculated
     */
    private void useProduct(Product product) {
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
        price = false;
        priceMin = Double.MAX_VALUE;
        priceMax = Double.MIN_VALUE;

        _new = false;
        used = false;
        for (int i = 0; i < size(); ++i) {
            if (get(i).getPrice() < min*0.01 || get(i).getPrice() > max*0.01) {
                remove(i);
                --i;
            } else
                useProduct(get(i));
        }
    }

    /**
     * Remove all used (not NEW) products
     */
    public void removeUsed() {
        if (!used)
            return;

        price = false;
        priceMin = Double.MAX_VALUE;
        priceMax = Double.MIN_VALUE;

        _new = false;
        used = false;
        for (int i = 0; i < size(); ++i) {
            if (get(i).getCondition() != Condition.NEW) {
                remove(i);
                --i;
            } else
                useProduct(get(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : this)
            stringBuilder.append(product.toString()).append('\n');
        return stringBuilder.toString();
    }

    /**
     * Return information about the group
     */
    @Override
    public JSONObject getJsonObject() {
        JSONObject object = new JSONObject();
        object.put("product_id", productID);

        //Add date_to
        //date_to and date_from are not null if the productgroup was loaded
        //for watchlist
        if(_from != null)
        {
            object.put("date_from", _from.toString());
        }
        if(_to != null)
        {
            object.put("date_to", _to.toString());
        }

        if (!Double.isNaN(averageRating))
            object.put("rating", averageRating);

        if (price) {
            object.put("price_min", priceMin);
            object.put("price_max", priceMax);
        }

        if (title != null)
            object.put("name", title);
        if (img != null)
            object.put("img", img);

        JSONArray array = new JSONArray();
        if (_new)
            array.add("NEW");
        if (used)
            array.add("USED");

        object.put("types", array);

        return object;
    }

    /**
     * Return all products in this group
     */
    @Override
    public JSONArray getJsonList() {
        JSONArray array = new JSONArray();
        for (Product product : this)
            array.add(product.getJsonObject());

        return array;
    }

    /**
     * Load the minimal price of the products in ProductGroup
     * @return the minimal price, NaN if the list is empty
     */
    public double getMinPrice()
    {
        if(size() == 0) return Double.NaN;
        double min = get(0).getPrice();
        for(Product p : this)
        {
            if(p.getPrice() < min)
            {
                min = p.getPrice();
            }
        }
        return min;
    }

    public void setGlobalId(int globalId)
    {
        this.productID = globalId;
    }
}
