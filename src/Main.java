import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Portfolio Manager");

        GridPane info = new GridPane();
        info.setVgap(10);
        info.setHgap(10);
        info.setAlignment(Pos.CENTER_LEFT);
        info.setPadding(new Insets(10,10,10,10));

        List<String> family = javafx.scene.text.Font.getFamilies();
        // TEXT objects
        Text stockText = new Text("Stock");
        Text numStocksText = new Text("Amount of Stock");
        Text initialPriceText = new Text("Initial Price of Stock");

        // TEXT FIELDS
        TextField stockID = new TextField();
        TextField numStocks = new TextField();
        TextField initialPrice = new TextField();


        info.add(stockText, 0, 0);
        info.add(numStocksText, 0, 1);
        info.add(initialPriceText, 0,2);
        info.add(stockID, 1, 0);
        info.add(numStocks, 1, 1);
        info.add(initialPrice, 1, 2);



        // Data Chart
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");
        LineChart dataChart = new LineChart(xAxis,yAxis);
        dataChart.setMaxSize(500, 200);

        // big Data
        GridPane headlineData = new GridPane();
        headlineData.setAlignment(Pos.CENTER);
        headlineData.setHgap(10);
        headlineData.setMinSize(200, 200);
        Text currVal = new Text();
        currVal.setStyle("-fx-font-size: 35pt");

        Polygon triangleChange = new Polygon();
        triangleChange.getPoints().addAll(new Double[]{0.0,0.0,0.0,0.0,0.0,0.0});
        Double[] greenTriangle = new Double[]{0.0, 10.0, 5.0, 0.0, 10.0, 10.0};
        Double[] redTriangle = new Double[]{0.0, 0.0, 5.0, 10.0, 10.0, 0.0};
        Text numChange = new Text();
        Text percentChange = new Text();

        GridPane val = new GridPane();
        val.setAlignment(Pos.CENTER_RIGHT);
        val.add(currVal,0,0);

        GridPane change = new GridPane();
        change.setAlignment(Pos.CENTER_RIGHT);
        change.setHgap(10);
        change.add(triangleChange, 0, 0);
        change.add(numChange, 1, 0);
        change.add(percentChange,2,0);

        GridPane update = new GridPane();
        Text lastUpdatedTime = new Text();
        Text lastUpdatedText = new Text();

        update.add( lastUpdatedText,0,0);
        update.add(lastUpdatedTime,0,1);

        headlineData.add(val, 0, 0);
        headlineData.add(change,0,1);
        headlineData.add(update,0,2);


        // root GRIDPANE
        GridPane root = new GridPane();
        root.setMinSize(1000,500);
        root.setGridLinesVisible(false);


        // Refresh Button
        Button refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.marketwatch.com/investing/stock/" + stockID.getText()).get();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                // TODO Invalid Stock or Null String Exception
                /*if (doc == null || doc.location().equals("https://marketwatch.com/tools/quotes/lookup.asp?lookup="+stockID.getText())) {
                    currVal.setText("N/A STOCK");
                    numChange.setText("");
                    percentChange.setText("");
                    lastUpdatedTime.setText("");
                    lastUpdatedText.setText("");
                    triangleChange.getPoints().setAll(0.0);
                    return;
                }*/
                currVal.setText("$" + WebScraper("value", doc));
                numChange.setText(WebScraper("change--point--q", doc));
                if (Double.parseDouble(numChange.getText()) >= 0) {
                    numChange.setFill(Color.GREEN);
                    percentChange.setFill(Color.GREEN);
                    for (int point = 0; point < 6; point++) {
                        triangleChange.getPoints().set(point, greenTriangle[point]);
                    }
                    triangleChange.setFill(Color.GREEN);
                } else {
                    numChange.setFill(Color.RED);
                    percentChange.setFill(Color.RED);
                    for (int point = 0; point < 6; point++) {
                        triangleChange.getPoints().set(point, redTriangle[point]);
                    }
                    triangleChange.setFill(Color.RED);
                }
                percentChange.setText(WebScraper("change--percent--q", doc));

                LocalDateTime currTime = LocalDateTime.now();
                lastUpdatedTime.setText(TimeFormatter(currTime));
                lastUpdatedText.setText("Last Updated: ");
            }
        });
        info.add(refresh, 0, 3);

        root.add(info,0,0);
        root.add(headlineData,1,0);
        root.add(dataChart, 2, 0);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static String WebScraper(String elementClass, Document doc) {
        if (doc == null) {
            return "";
        }
        Elements vals = doc.getElementsByClass(elementClass);
        for (Element e : vals) {
            return e.text();
        }
        return "";
    }

    public static String TimeFormatter(LocalDateTime time) {
        String rtn = "";
        int monthNum = time.getMonthValue();
        switch (monthNum) {
            case 1:
                rtn += "Jan ";
                break;
            case 2:
                rtn += "Feb ";
                break;
            case 3:
                rtn += "Mar ";
                break;
            case 4:
                rtn += "Apr ";
                break;
            case 5:
                rtn += "May ";
                break;
            case 6:
                rtn += "Jun ";
                break;
            case 7:
                rtn += "Jul ";
                break;
            case 8:
                rtn += "Aug ";
                break;
            case 9:
                rtn += "Sept ";
                break;
            case 10:
                rtn += "Oct ";
                break;
            case 11:
                rtn += "Nov ";
                break;
            case 12:
                rtn += "Dec ";
                break;
        }
        int hour = time.getHour();
        String hourString = "";
        String minuteString = "";
        if (time.getMinute() < 10) {
            minuteString += "0" + time.getMinute();
        } else {
            minuteString += time.getMinute();
        }
        if (hour == 0) {
            hour = 12;
            hourString = hour + ":" + minuteString + "a.m.";
        } else if (hour == 12) {
            hourString = hour + ":" + minuteString + "p.m.";
        } else if (hour > 12) {
            hour -= 12;
            hourString = hour + ":" + minuteString + "p.m.";
        } else {
            hourString = hour + ":" + minuteString + "a.m.";
        }
        rtn += time.getDayOfMonth() + ", " + time.getYear() + " " + hourString;
        return rtn;
    }
}
