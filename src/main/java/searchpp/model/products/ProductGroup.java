package searchpp.model.products;

import java.util.ArrayList;

/**
 * ProductGroup
 *
 * @version 1
 */
public class ProductGroup extends ArrayList<Product> {

    /**
     * Removed all products out of price range
     * @param min min price in cent
     * @param max max price in cent
     */
    public void setPrice(int min, int max) {
        for (int i = 0; i < size(); ++i) {
            if (get(i).getPrice() < min || get(i).getPrice() > max) {
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
}
