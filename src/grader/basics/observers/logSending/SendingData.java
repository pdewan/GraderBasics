package grader.basics.observers.logSending;

public class SendingData {

	private final String log, assignment;
	private final int iteration;
	
	public SendingData(String log, String assignment, int iteration) {
		this.log=log;
		this.assignment=assignment;
		this.iteration=iteration;
	}
	
	public String getLog() {
		return log;
	}
	
	public String getAssignment() {
		return assignment;
	}
	
	public int getIteration() {
		return iteration;
	}
}
