
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class TestClient extends AbstractVerticle {
	
	public static void main(String[] args) throws Exception {		
	
		String host = "localhost"; // "b1164b27.ngrok.io";
		int port = 8080;

		Vertx vertx = Vertx.vertx();

		WebClient client = WebClient.create(vertx);
		
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
	}
	
}
