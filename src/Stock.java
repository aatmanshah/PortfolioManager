import javafx.scene.chart.XYChart;
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
    String stockExchange;
    XYChart.Series chartData;

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

        currentPrice = Double.parseDouble(WebScraper("value", marketwatch, 0).replaceAll("," , ""));
        openingPrice = Double.parseDouble(WebScraper("kv__value kv__primary ", marketwatch, 0)
                    .substring(1).replaceAll(",",""));
        pointsChange = Double.parseDouble(WebScraper("change--point--q", marketwatch, 0));
        percentChange = Double.parseDouble(WebScraper("change--percent--q", marketwatch, 0).substring(0,4));
        name = WebScraper("company__name", marketwatch, 0);
        dayRange = WebScraper("kv__value kv__primary ", marketwatch, 1);
        yearRange = WebScraper("kv__value kv__primary " , marketwatch, 2);
        marketCap = WebScraper("kv__value kv__primary ", marketwatch, 3);
        sharesOutstanding = WebScraper("kv__value kv__primary ", marketwatch, 4);
        dividend = WebScraper("Ta(end) Fw(b) Lh(14px)", yahoo, 13);
        stockExchange = WebScraper("company__market", marketwatch, 0).substring(6);

        chartData = WebScraperChartData(ticker, stockExchange);
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

    public static XYChart.Series WebScraperChartData(String ticker, String stockExchange) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.google.com/search?tbm=fin&q="+stockExchange+":+"+ticker).get();
        } catch (IOException e) {
            return null;
        }
        Elements val = doc.getElementsByClass("_FHs line-z0");
        String path = "";
        for (Element element : val) {
            path = element.attr("d");
        }

        String delims = "[ ]";
        String[] coords = path.split(delims);

        double max=0;
        for (int i = 0; i < coords.length-9; i+=3) {
            double num = Double.parseDouble(coords[i+2]);
            if (num > max) {
                max = num;
            }
        }
        XYChart.Series rtn = new XYChart.Series();
        rtn.setName(ticker);
        for (int i = 0; i < coords.length-9; i+=3) {
            rtn.getData().add(new XYChart.Data(Double.parseDouble(coords[i+1]), max - Double.parseDouble(coords[i+2])));
        }

        return rtn;
    }
}
