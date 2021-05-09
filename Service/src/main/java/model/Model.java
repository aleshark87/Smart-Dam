package model;

import java.util.List;

import controller.DataPoint;

public interface Model {

    
    /* Metodi e costanti */
    
    public enum STATE{
        NORMAL, PRE_ALARM, ALARM
    }
    
    public void setUpdateTimesInternet(final double alarmTime, final double notAlarmTime);
    
    public List<Double> getTimes();
    
    public void handleNewData(final DataPoint data);
    
    public STATE getState();
    
    public float getDistance();
    
    public int getDamOpening();
    
    public boolean getManualMode();

}
