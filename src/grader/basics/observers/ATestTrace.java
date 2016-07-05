package grader.basics.observers;

public class ATestTrace {
	long time;
	String completed;
	String partial;
	String failed;
	String test;
	String score;
	public ATestTrace(long time, String completed, String partial,
			String failed,  String score) {
		super();
		this.time = time;
		this.completed = completed;
		this.partial = partial;
		this.failed = failed;
		this.test = test;
		this.score = score;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
	}
	public String getPartial() {
		return partial;
	}
	public void setPartial(String partial) {
		this.partial = partial;
	}
	public String getFailed() {
		return failed;
	}
	public void setFailed(String failed) {
		this.failed = failed;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String newVal) {
		score = newVal;
	}
	
	

}
