package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import io.vertx.core.Future;


public class MainSceneController {
    
    private LineChart<String, Number> lineChart;
    private XYChart.Series<String, Number> series;
    private final int WINDOW_SIZE = 20;
    private boolean changeToPreAlarm = true;
    private boolean firstExec = true;
    private String prevState = "UNDEFINED";
    private String state = "UNDEFINED";
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
        prevState = state;
        state = data.getState();
        if(state.equals("PRE_ALARM") && prevState.equals("NORMAL")) {
            changeToPreAlarm = true;
        }
        else {
            if(state.equals("PRE_ALARM") && firstExec) {
                changeToPreAlarm = true;
                firstExec = false;
            }
            else {
                changeToPreAlarm = false;
            }
        }
        String level = data.getState().equals("NORMAL") ? " > 20" : Double.toString(data.getLevel());
        String stateLevel = "State = " + data.getState() + ", Level = " + level;
        setLabelText(stateLevelLabel, stateLevel);
        String manualMode = "Manual mode = " + data.isManualMode();
        String damOpening = data.getState().equals("ALARM") ? ", Dam Opening = " + data.getDamOpening() : "";
        String manualDam = manualMode + damOpening;
        setLabelText(manualLabel, manualDam);
        updateChart(data);
    }
    
    private void updateChart(Data data) {
        
        Date dt = new Date(data.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateTime = sdf.format(dt);
        if(data.getState().equals("NORMAL")) {
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    series.getData().clear();
                    series.getData().add(new XYChart.Data<>(" ", 20));
                }
                
            });
        }
        else {
            if(changeToPreAlarm) {
                Future<List<Data>> futureData = controller.getConnection().getDataFromService(WINDOW_SIZE / 2);
                futureData.onComplete(req -> {
                    if(req.succeeded()) {
                        List<Data> results = req.result();
                        System.out.println(results.size());
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                series.getData().clear();
                                for(Data res : results) {
                                    series.getData().add(
                                            new XYChart.Data<>(res.getTimeString(), res.getDistanceRounded()));
                                }
                            }
                            
                        });
                    }
                 });
            }
            else {
                Platform.runLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        series.getData().add(
                                new XYChart.Data<>(dateTime, data.getLevel()));
                        if (series.getData().size() > WINDOW_SIZE)
                          series.getData().remove(0);
                    }
                    
                });
            }
        }
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
        
        final CategoryAxis xAxis;
        final NumberAxis yAxis;
        xAxis = new CategoryAxis(); yAxis = new NumberAxis();
        xAxis.setLabel("Time"); yAxis.setLabel("Level");
        xAxis.setAnimated(false); yAxis.setAnimated(true);
        
        // creating the line chart with two axis created above
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false); // disable animations

        // defining a series to display data
        series = new XYChart.Series<>();
        
        // add series to chart
        lineChart.getData().add(series);

        lineChart.setPrefSize(1280, 400);

        chartContainer.getChildren().addAll(lineChart);
    }
    
    
}
