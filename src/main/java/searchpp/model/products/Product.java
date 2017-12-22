package searchpp.model.products;

/**
 * Created by Tobi on 04.12.2017.
 */
public abstract class Product
{
    private int _globalId;
    /**
     * Product id
     */
    private String _productId;

    /**
     * Title of product
     */
    private String _title; //todo default title

    /**
     * {@link Condition} of product
     */
    private Condition _condition; //todo default condition

    /**
     * Price
     */
    private double _price; //todo default price

    private String _imgUrl;

    public Product()
    {}

    public int getGlobalId()
    {
        return _globalId;
    }
    public void setGlobalId(int globalId)
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

}
