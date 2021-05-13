package model;

public class ArduinoPoint {
    private int state;
    private int damOpening;
    private float distance;
    
    public ArduinoPoint(final int state, final int damOpening, final float distance) {
        this.state = state;
        this.damOpening = damOpening;
        this.distance = distance;
    }

    public int getState() {
        return state;
    }

    public int getDamOpening() {
        return damOpening;
    }

    public float getDistance() {
        return distance;
    }
}
