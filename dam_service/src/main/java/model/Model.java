package model;

import java.util.List;

/**
 * Model is where the Dam Service stores the information.
 * It allerts also the Serial Verticle when communication need to be done.
 * @author aless
 *
 */
public interface Model {

    
    public enum STATE{
        NORMAL, PRE_ALARM, ALARM
    }
    
    public void setUpdateTimesInternet(final double alarmTime, final double notAlarmTime);
    
    public List<Double> getTimes();
    
    public void handleNewData(final DataPoint data);
    
    public long getTime();
    
    public STATE getState();
    
    public float getDistance();
    
    public int getDamOpening();
    
    public boolean getManualMode();

}
