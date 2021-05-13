package controller;


import java.time.LocalDateTime;

import model.Model.STATE;

public class DataPoint {
	private STATE state;
	private int intState;
	private long time;
	private float distance;
	private LocalDateTime localTime;
	
	public DataPoint(int state, long time, float distance) {
		this.state = stateComputer(state);
		this.time = time;
		this.distance = distance;
	}
	
	public DataPoint(long time, float distance) {
	    this.time = time;
	    this.distance = distance;
	}
	
	public DataPoint(int state) {
	    this.state = stateComputer(state);
	}
	
	public DataPoint(LocalDateTime localTime, float distance) {
	    this.localTime = localTime;
	    this.distance = distance;
	}
	
	public String getDbTime() {
	    return localTime.toString();
	}
	
	private STATE stateComputer(int integerState) {
	    switch(integerState) {
	    case 0:
	        return STATE.NORMAL;
	    case 1:
	        return STATE.PRE_ALARM;
	    case 2:
	        return STATE.ALARM;
	    }
	    return STATE.NORMAL;
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
