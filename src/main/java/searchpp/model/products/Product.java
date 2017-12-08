package searchpp.model.products;

/**
 * Created by Tobi on 04.12.2017.
 */
public abstract class Product
{
    private String _productId;
    private String _title;
    private Condition _condition;
    private double _price;

    public void setProductId(String productId)
    {
        _productId = productId;
    }

    public void setTitle(String title)
    {
        _title = title;
    }

    public void setCondition(Condition condition)
    {
        _condition = condition;
    }

    public void setPrice(double price)
    {
        _price = price;
    }
}
