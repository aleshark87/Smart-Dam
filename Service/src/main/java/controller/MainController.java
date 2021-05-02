package controller;

import io.vertx.core.Vertx;
import model.Model;
import model.ModelImpl;

public class MainController {

    private SerialVerticle serialVerticle;
    private InternetVerticle internetVerticle;
    private DatabaseConnection connection;
    private Model model;
    
    public MainController() {
        serialVerticle = new SerialVerticle(this);
        internetVerticle = new InternetVerticle(this);
        model = new ModelImpl(this);
        //connection = new DatabaseConnection();
    }
    
    public void startServices() throws Exception {
        Vertx vertx = Vertx.vertx();
        
        vertx.deployVerticle(serialVerticle);
        vertx.deployVerticle(internetVerticle);
    }
    
    public Model getModel() {
        return model;
    }
    
    public SerialVerticle getSerialVerticle() {
        return serialVerticle;
    }
    
    public DatabaseConnection getConnection() {
        return connection;
    }
    
    public static void main(String[] args) throws Exception {
        MainController controller = new MainController();
        controller.startServices();
    }
}
