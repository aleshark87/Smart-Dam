package controllers;

import io.vertx.core.AbstractVerticle;
/**
 * Controller that connects the SceneController with the
 * HTTP connection of the client.
 * @author aless
 *
 */
public class Controller extends AbstractVerticle{
    private MainSceneController viewController;
    private HttpConnection connection;
    
    public Controller(MainSceneController view) {
        connection = new HttpConnection(this);
        viewController = view;
    }
    
    @Override
    public void start() {
        connection.start();
    }
    
    public MainSceneController getView() {
        return viewController;
    }
    
    public HttpConnection getConnection() {
        return connection;
    }
    
}
