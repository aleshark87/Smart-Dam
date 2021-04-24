package controller;

import io.vertx.core.AbstractVerticle;

public class AbstractServiceVerticle extends AbstractVerticle{
    private MainController controller;
    
    public AbstractServiceVerticle(MainController controller) {
        this.controller = controller;
    }
    
    public MainController getMainController() {
        return this.controller;
    }
}
