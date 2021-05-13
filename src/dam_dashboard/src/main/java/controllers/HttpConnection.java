package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

/**
 * Client connection with the server.
 * @author aless
 *
 */
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
                            e.printStackTrace();
                        }
                    }
                    changeScheduleTime(notAlarmTime);
                }
            }
            
        };
    }
    
    private boolean areTimesSetted() {
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
            var time = response.getJsonObject(0).getLong("time");
            if(state.equals("ALARM")) {
                changeScheduleTime(alarmTime);
                int damOpening = response.getJsonObject(0).getInteger("damOpening");
                controller.getView().dataUpdate(new Data(state, manualMode, level, damOpening, time));
            }
            else {
                if(!state.equals("NORMAL")) {
                    controller.getView().dataUpdate(new Data(state, manualMode, level, -1, time));
                }
                else {
                    controller.getView().dataUpdate(new Data(state, manualMode, level, -1, 0));
                }
                changeScheduleTime(notAlarmTime);
                
            }
            
        })
        .onFailure(err ->
          System.out.println("Something went wrong " + err.getMessage()));
        
    }
    
    public Future<List<Data>> getDataFromService(int dataNumber) {
        Promise<List<Data>> dataPromise = Promise.promise();
        
        client
        .get(port, host, "/api/data/levels")
        .send()
        .onSuccess(res -> {
                List<Data> dataIntern = new ArrayList<>();
                JsonArray response = res.bodyAsJsonArray();
                for (int i = (response.size() - 1); i >= 0; i--) {
                    JsonObject obj = response.getJsonObject(i);
                    dataIntern.add(new Data(obj.getString("time"), obj.getFloat("level")));
                }
                dataPromise.complete(dataIntern);
        });
        return dataPromise.future();
        
    }
}
