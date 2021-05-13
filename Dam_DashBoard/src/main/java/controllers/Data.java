package controllers;

public class Data {
    private String state;
    private boolean manualMode;
    private double level;
    private long time = 0;
    private int damOpening = -1;
    private String timeString = "";
    private float distance = 0.0f;
    
    public Data(String state, boolean manualMode, double level, int damOpening, long time) {
        this.state = state;
        this.manualMode = manualMode;
        this.level = level;
        this.damOpening = damOpening;
        this.time = time;
    }
    
    public Data(String timeString, float distance) {
        this.timeString = timeString;
        this.distance = distance;
    }
    
    public String getTimeString() {
        String smallData = timeString.split("T")[1];
        return smallData;
    }
    
    public double getDistanceRounded() {
        //DecimalFormat df = new DecimalFormat("###.##");
        return Math.round(distance*100.0)/100.0;
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

    public double getLevel() {
        return level;
    }
    
    public long getTime() {
        return time;
    }
}
