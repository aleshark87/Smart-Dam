package controller;

/**
 * Listener to communicate between SerialCommChannel and SerialVerticle
 * @author aless
 *
 */
public interface MsgEventListener{

    void msgArrived(final String msg);
}
