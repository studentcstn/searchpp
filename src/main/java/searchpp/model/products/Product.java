package searchpp.model.products;

import org.json.simple.JSONObject;
import searchpp.model.json.JsonObject;

/**
 * Created by Tobi on 04.12.2017.
 */
public abstract class Product implements JsonObject
{
    /**
     * Internal id
     */
    private long _globalId;
    /**
     * Product id
     */
    private String _productId;

    /**
     * Title of product
     */
    private String _title;

    /**
     * {@link Condition} of product
     */
    private Condition _condition;

    /**
     * Price
     */
    private double _price;

    /**
     * Url of product image
     */
    private String _imgUrl;

    /**
     * Url to product on website
     */
    private String _productUrl;

    public Product()
    {}

    public long getGlobalId()
    {
        return _globalId;
    }
    public void setGlobalId(long globalId)
    {
        _globalId = globalId;
    }

    public void setProductId(String productId) {
        _productId = productId;
    }
    public String getProductId() {
        return _productId;
    }


    public void setTitle(String title)
    {
        _title = title;
    }
    public String getTitle() {
        return _title;
    }


    public void setCondition(Condition condition)
    {
        _condition = condition;
    }
    public Condition getCondition() {
        return _condition;
    }


    public void setPrice(double price)
    {
       _price = price;
    }
    public double getPrice() {
        return _price;
    }

    public String getImgUrl()
    {
        return _imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        _imgUrl = imgUrl;
    }

    public String getProductUrl()
    {
        return _productUrl;
    }

    public void setProductUrl(String productUrl)
    {
        _productUrl = productUrl;
    }

    /**
     * Creates JSON Object from product
     * @return the JSON Object
     */
    @Override
    public JSONObject getJsonObject() {
        JSONObject object = new JSONObject();
        object.put("origin_id", _productId);
        object.put("origin_url", _productUrl);
        object.put("img", _imgUrl);
        object.put("name", _title);
        object.put("price", _price);
        object.put("type", (_condition == Condition.NEW ? "NEW" : "USED"));

        return object;
    }

    @Override
    public String toString() {
        return getProductId() + " " + getTitle();
    }
}
