package controller;

import io.vertx.core.AbstractVerticle;
/**
 * Simple class to get reference to controller in a AbstractVerticle
 * @author aless
 *
 */
public class AbstractServiceVerticle extends AbstractVerticle{
    private MainController controller;
    
    public AbstractServiceVerticle(MainController controller) {
        this.controller = controller;
    }
    
    public MainController getMainController() {
        return this.controller;
    }
}
