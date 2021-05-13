package controllers;

/**
 * Data gets together all the data needed by dashboard.
 * @author aless
 *
 */

public class Data {
    private String state;
    private boolean manualMode;
    private double level;
    private long time = 0;
    private int damOpening = -1;
    private String timeString = "";
    
    public Data(String state, boolean manualMode, double level, int damOpening, long time) {
        this.state = state;
        this.manualMode = manualMode;
        this.level = level;
        this.damOpening = damOpening;
        this.time = time;
    }
    
    public Data(String timeString, float level) {
        this.timeString = timeString;
        this.level = level;
    }
    
    public String getTimeString() {
        String smallData = timeString.split("T")[1];
        return smallData;
    }
    
    public double getLevelRounded() {
        return Math.round(level*100.0)/100.0;
    }
    
    public int getDamOpening(){
        return damOpening;
    }

    public String getState() {
        return state;
    }

    public String isManualMode() {
        return manualMode ? "On" : "Off";
    }
    
    public long getTime() {
        return time;
    }
}
