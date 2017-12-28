import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.time.LocalDateTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Portfolio Manager");

        // Input TextFields Pane
        GridPane info = new GridPane();
        info.setVgap(10);
        info.setHgap(10);
        info.setAlignment(Pos.CENTER);
        info.setPadding(new Insets(10,10,10,10));

        // TEXT objects
        Text stockText = new Text("Stock");
        Text numStocksText = new Text("Amount of Stock");
        Text initialPriceText = new Text("Initial Price of Stock");

        // TEXT FIELDS
        TextField stockID = new TextField();
        stockID.setMinWidth(150);
        TextField numStocks = new TextField();
        TextField initialPrice = new TextField();

        info.add(stockText, 0, 0);
        info.add(numStocksText, 0, 1);
        info.add(initialPriceText, 0,2);
        info.add(stockID, 1, 0);
        info.add(numStocks, 1, 1);
        info.add(initialPrice, 1, 2);

        // Data Chart
        // TODO Data Input and Scraping
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");
        yAxis.setMinorTickVisible(false);
        yAxis.setTickMarkVisible(false);
        yAxis.setTickLabelsVisible(false);
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");
        xAxis.setMinorTickVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelsVisible(false);
        LineChart dataChart = new LineChart(xAxis,yAxis);
        dataChart.setMaxSize(500, 200);
        dataChart.setCreateSymbols(false);

        // headlineData Pane
        GridPane headlineData = new GridPane();
        headlineData.setAlignment(Pos.CENTER);
        headlineData.setHgap(10);
        headlineData.setMinSize(200, 200);

        Polygon triangleChange = new Polygon();
        triangleChange.getPoints().addAll(new Double[]{0.0,0.0,0.0,0.0,0.0,0.0});
        Double[] greenTriangle = new Double[]{0.0, 10.0, 5.0, 0.0, 10.0, 10.0};
        Double[] redTriangle = new Double[]{0.0, 0.0, 5.0, 10.0, 10.0, 0.0};
        Text numChange = new Text();
        Text percentChange = new Text();

        GridPane val = new GridPane();
        val.setAlignment(Pos.CENTER_RIGHT);
        Text currVal = new Text();
        currVal.setStyle("-fx-font-size: 35pt");
        val.add(currVal,0,0);

        GridPane change = new GridPane();
        change.setAlignment(Pos.CENTER_RIGHT);
        change.setHgap(10);
        change.add(triangleChange, 0, 0);
        change.add(numChange, 1, 0);
        change.add(percentChange,2,0);

        GridPane update = new GridPane();
        update.setAlignment(Pos.CENTER_RIGHT);
        Text lastUpdatedTime = new Text();
        Text lastUpdatedText = new Text();
        update.add(lastUpdatedText,0,0);
        update.add(lastUpdatedTime,1,0);

        GridPane stockName = new GridPane();
        stockName.setAlignment(Pos.CENTER_RIGHT);
        Text name = new Text();
        name.setStyle("-fx-font-size: 14pt");
        stockName.add(name, 0,0);


        headlineData.add(stockName, 0, 0);
        headlineData.add(val, 0, 1);
        headlineData.add(change,0,2);
        headlineData.add(update,0,3);

        // Info of searched stock
        GridPane stockInfo = new GridPane();
        stockInfo.setVgap(5);
        stockInfo.setPadding(new Insets(10,10,10,10));
        Text openingPrice = new Text();
        Text dayRange = new Text();
        Text yearRange = new Text();
        Text marketCap = new Text();
        Text dividend = new Text();
        Text sharesOutstanding = new Text();

        stockInfo.add(openingPrice,1,0);
        stockInfo.add(dayRange,1,1);
        stockInfo.add(yearRange,1,2);
        stockInfo.add(marketCap,1,3);
        stockInfo.add(dividend,1,4);
        stockInfo.add(sharesOutstanding,1,5);

        // Refresh Button
        Button refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.marketwatch.com/investing/stock/" + stockID.getText()).get();
                } catch (java.io.IOException e) {
                    //e.printStackTrace();
                    return;
                }
                // If stockID does not exist
                if (doc.location().equals("https://www.marketwatch.com/tools/quotes/lookup.asp?lookup="+stockID.getText())) {
                    currVal.setText("N/A STOCK");
                    numChange.setText("");
                    percentChange.setText("");
                    lastUpdatedTime.setText("");
                    lastUpdatedText.setText("");
                    triangleChange.getPoints().setAll(new Double[]{0.0,0.0,0.0,0.0,0.0,0.0});

                    openingPrice.setText("");
                    dayRange.setText("");
                    yearRange.setText("");
                    marketCap.setText("");
                    dividend.setText("");
                    sharesOutstanding.setText("");
                    return;
                }
                Stock stock = new Stock(stockID.getText());
                currVal.setText("$" + stock.currentPrice);
                numChange.setText(Double.toString(stock.pointsChange));
                if (stock.pointsChange >= 0) {
                    numChange.setFill(Color.GREEN);
                    percentChange.setFill(Color.GREEN);
                    triangleChange.getPoints().setAll(greenTriangle);
                    triangleChange.setFill(Color.GREEN);
                } else {
                    numChange.setFill(Color.RED);
                    percentChange.setFill(Color.RED);
                    triangleChange.getPoints().setAll(redTriangle);
                    triangleChange.setFill(Color.RED);
                }
                percentChange.setText(Double.toString(stock.percentChange) + "%");

                LocalDateTime currTime = LocalDateTime.now();
                lastUpdatedTime.setText(TimeFormatter(currTime));
                lastUpdatedText.setText("Last Updated: ");
                name.setText(stock.name);

                openingPrice.setText("Opening Price: $" + Double.toString(stock.openingPrice));
                dayRange.setText("Day Range: " + stock.dayRange);
                yearRange.setText("52 Week Range: " + stock.yearRange);
                marketCap.setText("Market Cap: " + stock.marketCap);
                dividend.setText("Dividend & Yield: " + stock.dividend);
                sharesOutstanding.setText("Shares Outstanding: " + stock.sharesOutstanding);

                dataChart.getData().add(stock.chartData);
            }
        });
        info.add(refresh, 0, 3);

        // Clear Button
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stockID.setText("");
                numStocks.setText("");
                initialPrice.setText("");
                if (dataChart.getData().size() > 0) {
                    int size = dataChart.getData().size();
                    dataChart.getData().remove(0,size);
                }
            }
        });

        // Add stock to User stocks
        Button addButton = new Button("Add to Portfolio");

        GridPane addClear = new GridPane();
        addClear.setHgap(10);
        addClear.add(addButton, 1, 0);
        addClear.add(clearButton, 0, 0);
        info.add(addClear, 1, 3);

        //Table of User stocks
        TableView table = new TableView();
        final Label label = new Label("Portfolio");

        table.setEditable(false);

        TableColumn tickerCol = new TableColumn("Stock Ticker");
        tickerCol.setMinWidth(100);
        tickerCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("name"));

        TableColumn shareCol = new TableColumn("Shares");
        shareCol.setMinWidth(100);
        shareCol.setCellValueFactory(new PropertyValueFactory<Stock, String>(numStocks.getText()));

        TableColumn initPriceCol = new TableColumn("Buy Price");
        initPriceCol.setMinWidth(100);
        initPriceCol.setCellValueFactory(new PropertyValueFactory<Stock, String>(initialPrice.getText()));

        TableColumn currentPriceCol = new TableColumn("Current Price");
        currentPriceCol.setMinWidth(100);
        currentPriceCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("current price"));

        TableColumn percentChangeCol = new TableColumn("% Change");
        percentChangeCol.setMinWidth(100);
        percentChangeCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("percent change"));

        TableColumn unrealizedCol = new TableColumn("UR L/G");
        unrealizedCol.setMinWidth(100);
        unrealizedCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("ur l/g"));

        table.getColumns().addAll(tickerCol);
        table.getColumns().addAll(shareCol);
        table.getColumns().addAll(initPriceCol);
        table.getColumns().addAll(currentPriceCol);
        table.getColumns().addAll(percentChangeCol);

        // root GRIDPANE
        GridPane root = new GridPane();
        GridPane top = new GridPane();
        GridPane bottom = new GridPane();
        root.add(top, 0,0);
        root.add(bottom, 0,1);
        root.setMinSize(1000,500);
        top.add(info,0,0);
        top.add(headlineData,1,0);
        top.add(dataChart, 2, 0);
        bottom.add(table, 0, 0);
        bottom.add(stockInfo,1,0);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /*
    Formatting function to make the last updated time more readable. Output format example: Dec 24, 2017 1:23 a.m.
     */
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
            hourString = hour + ":" + minuteString + " a.m.";
        } else if (hour == 12) {
            hourString = hour + ":" + minuteString + " p.m.";
        } else if (hour > 12) {
            hour -= 12;
            hourString = hour + ":" + minuteString + " p.m.";
        } else {
            hourString = hour + ":" + minuteString + " a.m.";
        }
        rtn += time.getDayOfMonth() + ", " + time.getYear() + " " + hourString;
        return rtn;
    }
}
