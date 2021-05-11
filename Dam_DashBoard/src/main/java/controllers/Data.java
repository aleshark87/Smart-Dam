package controllers;

import java.util.Optional;

public class Data {
    private String state;
    private boolean manualMode;
    private double level;
    private int damOpening = -1;
    
    public Data(String state, boolean manualMode, double level, int damOpening) {
        this.state = state;
        this.manualMode = manualMode;
        this.level = level;
        this.damOpening = damOpening;
    }
    
    public Data(String state, boolean manualMode, double level) {
        this.state = state;
        this.manualMode = manualMode;
        this.level = level;
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
}
