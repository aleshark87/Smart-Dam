package controller;


import model.Model.STATE;

public class DataPoint {
	private STATE state;
	private int intState;
	private long time;
	private float distance;
	
	public DataPoint(int state, long time, float distance) {
		this.state = stateComputer(state);
		this.time = time;
		this.distance = distance;
	}
	
	private STATE stateComputer(int integerState) {
	    intState = integerState;
	    if(integerState == 1) {
	        return STATE.PRE_ALARM;
	    }
	    else {
	        return STATE.ALARM;
	    }
	}
	
	public STATE getState() {
		return state;
	}
	
	public int getIntegerState() {
	    return intState;
	}
	
	public long getTime() {
		return time;
	}
	
	public float getDistance() {
		return distance;
	}
}
