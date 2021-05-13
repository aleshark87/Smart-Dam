package controller;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import model.Model.STATE;

/*
 * Data Service as a vertx event-loop 
 */
public class InternetVerticle extends AbstractServiceVerticle {

	private static final int port = 8080;

	public InternetVerticle(MainController controller) {
	    super(controller);
	}

	@Override
	public void start() {		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.post("/api/data").handler(this::handleAddNewData);
		router.get("/api/data/times").handler(this::handleGetTimes);
		router.get("/api/data/levels").handler(this::handleLevels);
		router.get("/api/data").handler(this::handleGetData);
		vertx
			.createHttpServer()
			.requestHandler(router)
			.listen(port);

		log("Service ready.");
	}
	
	// POST handler
	private void handleAddNewData(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		//log("new msg "+routingContext.getBodyAsString());
		JsonObject res = routingContext.getBodyAsJson();
		if (res == null) {
			sendError(400, response);
		} else {
	        String type = res.getString("type");
            if(type.equals("data")) {
                int state = res.getInteger("state");
                float distance = res.getFloat("distance");
                long time = System.currentTimeMillis();
                DataPoint data = new DataPoint(state, time, distance);
                this.getMainController().getModel().handleNewData(data);
                response.setStatusCode(200).end();
            }
            if(type.equals("state")) {
                int state = res.getInteger("state");
                DataPoint statePoint = new DataPoint(state);
                this.getMainController().getModel().handleNewData(statePoint);
                response.setStatusCode(200).end();
            }
            if(type.equals("times")) {
                this.getMainController().getModel().setUpdateTimesInternet(res.getDouble("prealarm_time"), res.getDouble("alarm_time"));
                response.setStatusCode(200).end();
            }
		}
	}

	private void handleGetData(RoutingContext routingContext) {
	    String state = getMainController().getModel().getState().toString();
	    JsonArray arr = new JsonArray();
        JsonObject data = new JsonObject();
        data.put("state", state);
        data.put("manualMode", getMainController().getModel().getManualMode());
        data.put("level", getMainController().getModel().getDistance());
        if(!state.equals("NORMAL")) {
            if(state.equals("ALARM")) {
                data.put("damOpening", getMainController().getModel().getDamOpening());
            }
            data.put("time", getMainController().getModel().getTime());
        }
        arr.add(data);
        //System.out.println(arr.encode());
        routingContext.response()
            .putHeader("content-type", "application/json")
            .end(arr.encode());
	}
	
	private void handleGetTimes(RoutingContext routingContext) {
	    System.out.println("giving times");
	    JsonArray arr = new JsonArray();
        JsonObject data = new JsonObject();
	    List<Double> timesList = getMainController().getModel().getTimes();
	    if(timesList != null) {
	        data.put("AlarmTime", timesList.get(1));
	        data.put("NotAlarmTime", timesList.get(0));
	        arr.add(data);
	    }
	    else {
	        data.put("null", "null");
	        arr.add(data);
	    }
		routingContext.response()
			.putHeader("content-type", "application/json")
			.end(arr.encode());
		
	}
	
	private void handleLevels(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        Future<RowSet<Row>> lastLevels = getMainController().getConnection().getLatestData(20);
        
        lastLevels.onComplete(req -> {
            if(req.succeeded()) {
                RowSet<Row> results = req.result();
                
                results.forEach(value -> {
                    JsonObject obj = new JsonObject();
                    obj.put("time", value.getLocalDateTime(0).toString());
                    obj.put("level", value.getFloat(1));
                    arr.add(obj);
                });
                
                routingContext.response()
                .putHeader("content-type", "application/json")
                .end(arr.encode());
            }
         });
        
        
    }
	
	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	private void log(String msg) {
		System.out.println("[DATA SERVICE] "+msg);
	}
}