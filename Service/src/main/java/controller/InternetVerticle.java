package controller;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import model.Model.STATE;

import java.util.LinkedList;

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
		log("new msg "+routingContext.getBodyAsString());
		JsonObject res = routingContext.getBodyAsJson();
		if (res == null) {
			sendError(400, response);
		} else {
			int state = res.getInteger("state");
			float distance = res.getFloat("distance");
			long time = System.currentTimeMillis();
			
			DataPoint data = new DataPoint(state, time, distance);
			
			this.getMainController().getModel().handleNewData(data);
			
			response.setStatusCode(200).end();
		}
	}
	// GET hadler
	private void handleGetData(RoutingContext routingContext) {
	   //Serve al client per ora cazzomene
		/*JsonArray arr = new JsonArray();
		for (DataPoint p: values) {
			JsonObject data = new JsonObject();
			data.put("time", p.getTime());
			data.put("value", p.getValue());
			data.put("place", p.getPlace());
			arr.add(data);
		}
		routingContext.response()
			.putHeader("content-type", "application/json")
			.end(arr.encodePrettily());
		*/
	}
	
	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	private void log(String msg) {
		System.out.println("[DATA SERVICE] "+msg);
	}
}