package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Portfolio Manager");

        GridPane info = new GridPane();
        info.setVgap(10);
        info.setHgap(10);
        info.setAlignment(Pos.CENTER_LEFT);
        info.setPadding(new Insets(10,10,10,10));


        // TEXT objects
        Text stockText = new Text("Stock");
        Text numStocksText = new Text("Amount of Stock");
        Text initialPriceText = new Text("Initial Price of Stock");

        // TEXT FIELDS
        TextField stockID = new TextField();
        TextField numStocks = new TextField();
        TextField initialPrice = new TextField();

        // Buttons
        Button refresh = new Button("Refresh");

        info.add(stockText, 0, 0);
        info.add(numStocksText, 0, 1);
        info.add(initialPriceText, 0,2);
        info.add(stockID, 1, 0);
        info.add(numStocks, 1, 1);
        info.add(initialPrice, 1, 2);
        info.add(refresh, 0, 3);


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
        Text currVal = new Text("$12345.34");
        currVal.setStyle("-fx-font: 35 arial");

        Polygon triangleChange = new Polygon();
        triangleChange.getPoints().addAll(new Double[]{0.0, 10.0, 5.0, 0.0, 10.0, 10.0});
        triangleChange.setFill(Color.GREEN);
        Text numChange = new Text(" +2.34");
        numChange.setFill(Color.GREEN);
        Text percentChange = new Text("+0.05%");
        percentChange.setFill(Color.GREEN);

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
        LocalDateTime currTime = LocalDateTime.now();
        lastUpdatedTime.setText(currTime.toString());
        update.add(new Text("Last Updated: "),0,0);
        update.add(lastUpdatedTime,0,1);

        headlineData.add(val, 0, 0);
        headlineData.add(change,0,1);
        headlineData.add(update,0,2);


        // root GRIDPANE
        GridPane root = new GridPane();
        root.setMinSize(1000,500);
        root.setGridLinesVisible(false);


        root.add(info,0,0);
        root.add(headlineData,1,0);
        root.add(dataChart, 2, 0);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
