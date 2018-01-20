package searchpp.model.products;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import searchpp.model.json.JsonList;
import searchpp.model.json.JsonObject;

class Rating implements JsonObject {
    public double percent;
    public int rating;
    public String url = "";

    public double getPercent() {
        return percent;
    }

    public int getRating() {
        return rating;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format("%d stars: %4.1f %%   url: %s", rating, percent, url);
    }

    @Override
    public JSONObject getJsonObject(){
        JSONObject rating = new JSONObject();
        rating.put("percent", getPercent());
        rating.put("rating", getRating());
        rating.put("url", getUrl());

        return rating;
    }
}

public class AmazonProductRating implements Comparable<AmazonProductRating>, JsonList {
    private AmazonProduct product;
    private Rating[] ratings = new Rating[5];
    private double averageRating = 0;
    private int allRatings = 0;

    public AmazonProductRating(AmazonProduct product) {
        this.product = product;
    }

    public Rating[] getAllRatings() {
        return ratings;
    }

    public Rating getRating(int starts) {
        if (starts < 0)
            starts = 1;
        if (starts > 5)
            starts = 5;
        return ratings[starts - 1];
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getRatingsCount() {
        return allRatings;
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
        ratings[position].percent = Double.parseDouble(rating.substring(0, rating.length() - 1));
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

    /**
     * internal rating system
     *
     * The internal rating system is used for a simpler sorting of products.
     * The rating works by assuming that many reviews say more about the product.
     * Which also means that at a average rating of 1.0, the product is actually bad and actually good at 5.0.
     * And with only a few reviews, nothing can be said about the product.
     */
    private double productRating = Double.NaN;
    private void calcProductRating() {
        productRating = allRatings + allRatings * (allRatings * 0.25 * (averageRating - 3.) * (averageRating - 3.) * (averageRating - 3.));
    }
    public double getInternProductRating() {
        if (Double.isNaN(productRating))
            calcProductRating();
        return productRating;
    }

    @Override
    public int compareTo(AmazonProductRating o) {
        if (Double.isNaN(productRating))
            calcProductRating();
        if (Double.isNaN(o.productRating))
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
        stringBuilder.append(String.format("  intern product rating: %f", getInternProductRating()));
        return stringBuilder.toString();
    }

    /**
     * Returns ratings
     * @return Ratings in a JsonList
     */
    @Override
    public JSONArray getJsonList() {
        JSONArray array = new JSONArray();
        for (Rating rating : ratings)
            array.add(rating.getJsonObject());

        return array;
    }
}
