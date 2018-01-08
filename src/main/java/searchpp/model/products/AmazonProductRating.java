package searchpp.model.products;

import org.json.simple.JSONObject;

class Rating {
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
    public JSONObject getJsonItem(){
        JSONObject rating = new JSONObject();
        rating.put("percent", getPercent());
        rating.put("raiting", getRating());
        rating.put("url", getUrl());

        return rating;
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

    private double productRating = -1.;
    private void calcProductRating() {
        //is a test. I do not know if it is working
        //productRating = allRatings - allRatings * (1./25.) * (-(averageRating * averageRating) + 25.);
        //better rating system
        productRating = allRatings + allRatings * (allRatings * 0.25 * (averageRating - 3.) * (averageRating - 3.) * (averageRating - 3.));
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
    public JSONObject getJsonItem(){
        JSONObject amazonPRaiting = new JSONObject();
        amazonPRaiting.put("product", product);
        amazonPRaiting.put("ratings",ratings);
        amazonPRaiting.put("averageRating", averageRating);
        amazonPRaiting.put("allRatings", allRatings);

        return amazonPRaiting;
    }
}
