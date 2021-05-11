package controllers;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import javafx.application.Platform;

public class HttpConnection extends AbstractVerticle{
	
    private Controller controller;
    private final String host = "localhost";
    private final int port = 8080;
    private WebClient client;
    private ScheduledExecutorService exec;
    private Runnable task;
    private long actualPeriod = 5000;
    private Double alarmTime = 0.0;
    private Double notAlarmTime = 0.0;
    private boolean timesSetted = false;
    private String state = "UNDEFINED";
    
    public HttpConnection(Controller controller) {
        this.controller = controller;
        task = new Runnable() {

            @Override
            public void run() {
                if(timesSetted) {
                    getData();
                }
                else {
                    while(!areTimesSetted()) {
                        getTimes();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    changeScheduleTime(notAlarmTime);
                }
            }
            
        };
    }
    
    private boolean areTimesSetted() {
        System.out.println("a");
        return timesSetted;
    }
    
    @Override
    public void start() {
        Vertx vertx = Vertx.vertx();
        client = WebClient.create(vertx);
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(task, 0, actualPeriod, TimeUnit.MILLISECONDS);
    }
    
    public void changeScheduleTime(double period) {
        long longPeriod = (long) (period * 1000);  
        if(longPeriod != actualPeriod) {
            System.out.println("switching to " + longPeriod);
            actualPeriod = longPeriod;
            exec.scheduleWithFixedDelay(task, 0L, actualPeriod, TimeUnit.MILLISECONDS);
        }
        
    }
    
    public void getTimes() {
        client
          .get(port, host, "/api/data/times")
          .send()
          .onSuccess(res -> { 
              JsonArray response = res.bodyAsJsonArray();
              if(!(response.getJsonObject(0).containsKey("null"))) {
                  alarmTime = response.getJsonObject(0).getDouble("AlarmTime");
                  notAlarmTime = response.getJsonObject(0).getDouble("NotAlarmTime");
                  if(alarmTime != 0.0 && notAlarmTime != 0.0) {
                      timesSetted = true;
                  }
              }
          })
          .onFailure(err ->
            System.out.println("Something went wrong " + err.getMessage()));
    }
    
    public void getData() {
        client
        .get(port, host, "/api/data")
        .send()
        .onSuccess(res -> { 
            JsonArray response = res.bodyAsJsonArray();
            state = response.getJsonObject(0).getString("state");
            var manualMode = response.getJsonObject(0).getBoolean("manualMode");
            var level = response.getJsonObject(0).getDouble("level");
            if(state.equals("ALARM")) {
                changeScheduleTime(alarmTime);
                int damOpening = response.getJsonObject(0).getInteger("damOpening");
                controller.getView().dataUpdate(new Data(state, manualMode, level, damOpening));
            }
            else {
                changeScheduleTime(notAlarmTime);
                controller.getView().dataUpdate(new Data(state, manualMode, level));
            }
            
        })
        .onFailure(err ->
          System.out.println("Something went wrong " + err.getMessage()));
        
    }
    
/*
		
		System.out.println("Getting data items... ");
		client
		  .get(port, host, "/api/data")
		  .send()
		  .onSuccess(res -> { 
			  System.out.println("Getting - Received response with status code: " + res.statusCode());
			  JsonArray response = res.bodyAsJsonArray();
		      System.out.println(response.encodePrettily());
		  })
		  .onFailure(err ->
		    System.out.println("Something went wrong " + err.getMessage()));
	*/
}
