package searchpp.model.products;

class Rating {
    public String percent;
    public int rating;
    public String url = "";

    @Override
    public String toString() {
        return String.format("%d stars: %3s   url: %s", rating, percent, url);
    }
}

public class AmazonProductRating implements Comparable<AmazonProductRating> {
    private AmazonProduct product;
    private Rating[] ratings = new Rating[5];
    private double averageRating = 0;
    private int allRatings = 0;

    public AmazonProductRating(AmazonProduct product) {
        this.product = product;
    }


    public void addUrl(String title, String url) {
        String[] titleSplit = title.split(" ");
        int position = Integer.parseInt(titleSplit[4]) - 1;
        if (ratings[position] == null)
            ratings[position] = new Rating();
        ratings[position].url = url;
    }
    public void addRating(String stars, String rating) {
        String[] starsSplit = stars.split(" ");
        int position = Integer.parseInt(starsSplit[0]) - 1;
        if (ratings[position] == null)
            ratings[position] = new Rating();
        ratings[position].percent = rating;
        ratings[position].rating = position + 1;
    }

    public void addAverageRating(String average) {
        averageRating = Double.parseDouble(average.split(" ")[0]);
    }

    public void addAllRatings(String allRatings) {
        try {
            this.allRatings = Integer.parseInt(allRatings.split(" ")[1]);
        } catch (Exception e) {
            if (allRatings.equals("Bewertung .*"))
                this.allRatings = 1;
            else if (allRatings.matches("Beide .*"))
                this.allRatings = 2;
            else
                this.allRatings = 0;
        }
    }


    private double productRating = -1.;
    private void calcProductRating() {
        //is a test. I do not know if it is working
        productRating = allRatings - allRatings * (1./25.) * (-(averageRating * averageRating) + 25.);
    }

    @Override
    public int compareTo(AmazonProductRating o) {
        if (productRating < 0)
            calcProductRating();
        if (o.productRating < 0)
            o.calcProductRating();
        return Double.compare(productRating, o.productRating);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Amazon product id: %s\n", product.getProductId()));
        stringBuilder.append(String.format("  %.1f starts from %d ratings\n", averageRating, allRatings));
        for (Rating rating : ratings) {
            if (rating != null)
                stringBuilder.append("  ").append(rating.toString()).append('\n');
        }
        if (productRating < 0)
            calcProductRating();
        stringBuilder.append(String.format("  intern product rating: %f", productRating));
        return stringBuilder.toString();
    }
}
