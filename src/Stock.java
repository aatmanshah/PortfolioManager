import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;

/**
 * Created by pingp on 12/26/2017.
 */
public class Stock {

    String ticker;
    String name;
    double openingPrice;
    double currentPrice;
    double percentChange;
    double pointsChange;
    String dayRange;
    String yearRange;
    String marketCap;
    String dividend;
    String sharesOutstanding;

    Stock(String tick) {
        ticker = tick;
        Document yahoo = null;
        Document marketwatch = null;
        try {
            yahoo = Jsoup.connect("https://www.finance.yahoo.com/quote/" + ticker + "?p=" + ticker).get();
            marketwatch = Jsoup.connect("https://www.marketwatch.com/investing/stock/" + ticker).get();
        } catch (IOException e) {
            return;
        }
        currentPrice = Double.parseDouble(WebScraper("value", marketwatch, 0));
        pointsChange = Double.parseDouble(WebScraper("change--point--q", marketwatch, 0));
        percentChange = Double.parseDouble(WebScraper("change--percent--q", marketwatch, 0).substring(0,4));
        name = WebScraper("company__name", marketwatch, 0);
        openingPrice = Double.parseDouble(WebScraper("kv__value kv__primary ", marketwatch, 0).substring(1));
        dayRange = WebScraper("kv__value kv__primary ", marketwatch, 1);
        yearRange = WebScraper("kv__value kv__primary " , marketwatch, 2);
        marketCap = WebScraper("kv__value kv__primary ", marketwatch, 3);
        sharesOutstanding = WebScraper("kv__value kv__primary ", marketwatch, 4);
        dividend = WebScraper("Ta(end) Fw(b) Lh(14px)", yahoo, 13);
    }

    /*
    Web Scraping function using jsoup. Takes in a string that is the element's class, found through inspect element, and
    the document that we are parsing through.
     */
    public static String WebScraper(String elementClass, Document doc, int order) {
        if (doc == null) {
            return "";
        }
        Elements vals = doc.getElementsByClass(elementClass);
        int counter = 0;
        for (Element e : vals) {
            if (counter == order) {
                return e.text();
            } else {
                counter++;
            }
        }
        return "";
    }
}
