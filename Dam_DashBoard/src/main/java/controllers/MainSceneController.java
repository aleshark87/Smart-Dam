package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainSceneController {
    
    private Controller controller = new Controller(this);
    
    @FXML
    private Label stateLevelLabel;
    
    @FXML
    private Label manualLabel;
    
    @FXML
    private Label labelChart;

    @FXML
    private VBox chartContainer;
    
    @FXML
    private Label titleLabel;
    
    public void dataUpdate(Data data) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                String level = data.getState().equals("NORMAL") ? " > 20" : Double.toString(data.getLevel());
                String stateLevel = "State = " + data.getState() + ", Level = " + level;
                setLabelText(stateLevelLabel, stateLevel);
                String manualMode = "Manual mode = " + data.isManualMode();
                String damOpening = data.getState().equals("ALARM") ? ", Dam Opening = " + data.getDamOpening() : "";
                String manualDam = manualMode + damOpening;
                setLabelText(manualLabel, manualDam);
            }
            
        });
    }
    
    private void setLabelText(Label lbl, String text) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                lbl.setText(text);
                
            }
            
        });
        
    }
    @FXML
    private void initialize() {
        setLabelText(stateLevelLabel, "State = UNDEFINED, Level = UNDEFINED");
        setLabelText(manualLabel, "Manual Mode = UNDEFINED");
        controller.start();
        //final List<CategoryAxis> xAxis
        /*
        final List<CategoryAxis> xAxis = new ArrayList<>();
        final List<NumberAxis> yAxis = new ArrayList<>();
        //for(int i = 0; i < NUM_GRAPHS; i++) {
            var xAxisIstance = new CategoryAxis(); var yAxisIstance = new NumberAxis();
            xAxisIstance.setLabel("Time"); xAxisIstance.setAnimated(false);
            yAxisIstance.setAnimated(false);
            xAxis.add(xAxisIstance); yAxis.add(yAxisIstance);
        //}
        yAxis.get(0).setLabel("Speed(Cm/S)"); yAxis.get(1).setLabel("Pos(Cm)"); yAxis.get(2).setLabel("Acc(Cm/s^2)");
        
        // creating the line chart with two axis created above
        final LineChart<String, Number> lineChartSpeed = new LineChart<>(xAxis.get(0), yAxis.get(0));
        final LineChart<String, Number> lineChartPos = new LineChart<>(xAxis.get(1), yAxis.get(1));
        final LineChart<String, Number> lineChartAcc = new LineChart<>(xAxis.get(2), yAxis.get(2));
        lineChartSpeed.setAnimated(false); // disable animations
        lineChartPos.setAnimated(false);
        lineChartAcc.setAnimated(false);

        // defining a series to display data
        XYChart.Series<String, Number> seriesSpeed = new XYChart.Series<>();
        seriesSpeed.setName("Speed");

        XYChart.Series<String, Number> seriesPos = new XYChart.Series<>();
        seriesPos.setName("Pos");
        
        XYChart.Series<String, Number> seriesAcc = new XYChart.Series<>();
        seriesAcc.setName("Acc");

        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Paint paint = Color.BLACK;
        startTimeMillis = System.currentTimeMillis();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                //seriesSpeed.getData().add(new XYChart.Data<>(currentTime, Integer.parseInt(speed)));
                 
                if (seriesSpeed.getData().size() > WINDOW_SIZE)
                    seriesSpeed.getData().remove(0);
            });
        }, 0, 20, TimeUnit.MILLISECONDS);
        
        // add series to chart
        lineChartSpeed.getData().add(seriesSpeed);

        lineChartSpeed.setPrefSize(1280, 350);

        //containerLineChart.getChildren().addAll(lineChartAcc);*/
    }
}
