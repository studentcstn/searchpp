package searchpp.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchpp.model.products.AmazonProduct;
import searchpp.model.products.AmazonProductRating;

public class AmazonRating {
    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";

    static public AmazonProductRating getRating(AmazonProduct product) {
        userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36 OPR/42.0.2393.94";

        String requestUrl = "https://www.amazon.de/review/widgets/average-customer-review/popover/ref=acr_search__popover?ie=UTF8&asin="
                + product.getProductId()
                + "&contextId=search&ref=acr_search__popover";

        AmazonProductRating apr;

        try {
            Document document = Jsoup.connect(requestUrl).header("User-Agent", userAgent).get();

            Elements elements = document.getElementsByClass("a-link-normal");

            apr = new AmazonProductRating(product);

            for(int i = 0; i < elements.size(); i+=2) {
                Element element = elements.get(i);

                apr.addUrl(element.attr("title"), element.attr("href"));
            }

            elements = document.getElementsByClass("a-size-small");
            for (int i = 1; i < elements.size(); i+=2) {
                Element elementStar = elements.get(i - 1);
                Element elementPercent = elements.get(i);

                apr.addRating(elementStar.text(), elementPercent.text());
            }


            elements = document.getElementsByClass("a-size-base a-color-secondary");
            Element element = elements.get(0);
            apr.addAverageRating(element.text());

            elements = document.getElementsByClass("a-size-small a-link-emphasis");
            element = elements.get(0);
            apr.addAllRatings(element.text());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



        return apr;
    }
}
