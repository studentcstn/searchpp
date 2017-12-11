package searchpp.model.products;

/**
 * Created by Tobi on 04.12.2017.
 */
public abstract class Product
{
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

    public Product() {}
    public Product(String productId) {
        this._productId = productId;
    }

    public void setProductId(String productId) {
        this._productId = productId;
    }
    public String getProductId() {
        return _productId;
    }


    public void setTitle(String title)
    {
        this._title = title;
    }
    public String getTitle() {
        return _title;
    }


    public void setCondition(Condition condition)
    {
        this._condition = condition;
    }
    public Condition getCondition() {
        return _condition;
    }


    public void setPrice(double price)
    {
       this._price = price;
    }
    public double getPrice() {
        return _price;
    }
}
