package grader.basics.observers;

public class ATestRunTrace {
	long time;
	String className;
	double fractionChange; 
	String message;
	public ATestRunTrace(long time, String className, double fractionChange,
			String message) {
		super();
		this.time = time;
		this.className = className;
		this.fractionChange = fractionChange;
		this.message = message;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public double getFractionChange() {
		return fractionChange;
	}
	public void setFractionChange(double fractionChange) {
		this.fractionChange = fractionChange;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
