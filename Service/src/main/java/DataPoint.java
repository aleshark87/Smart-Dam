
class DataPoint {
	private int state;
	private long time;
	private float distance;
	
	public DataPoint(int state, long time, float distance) {
		this.state = state;
		this.time = time;
		this.distance = distance;
	}
	
	public int getState() {
		return state;
	}
	
	public long getTime() {
		return time;
	}
	
	public float getDistance() {
		return distance;
	}
}
