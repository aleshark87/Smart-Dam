package model;

import controller.DataPoint;

public interface Model {

    
    /* Metodi e costanti */
    
    public enum STATE{
        NORMAL, PRE_ALARM, ALARM
    }
    public void handleNewData(final DataPoint data);
    
    public STATE getState();
    
    public float getDistance();
    
    public int getDamOpening();

}
