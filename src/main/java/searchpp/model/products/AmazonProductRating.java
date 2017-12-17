package searchpp.model.products;

class Rating {
    public String percent;
    public int num;
    public int rating;
    public String url;
}

public class AmazonProductRating implements Comparable<AmazonProductRating> {
    private Rating[] ratings = new Rating[5];
    private double averageRating = 0;
    private int allRatings = 0;

    public void addRating(String title, String url) {
        String[] titleSplit = title.split(" ");
        int position = Integer.parseInt(titleSplit[4]) - 1;
        ratings[position] = new Rating();
        ratings[position].percent = titleSplit[0];
        ratings[position].rating = position + 1;
        ratings[position].url = url;
    }

    public void addAverageRating(String average) {
        averageRating = Double.parseDouble(average.split(" ")[0]);
    }

    public void addAllRatings(String allRatings) {
        this.allRatings = Integer.parseInt(allRatings.split(" ")[1]);
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
}
