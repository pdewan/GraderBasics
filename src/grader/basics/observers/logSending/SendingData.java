package grader.basics.observers.logSending;

import grader.basics.observers.LogEntryKind;

public class SendingData {
	private final String logFileName;
	private final String log, assignment;
	private final int iteration;
//	private  boolean isTests = false;
	private LogEntryKind logEntryKind = LogEntryKind.TEST;
	
//	public SendingData(boolean anIsTests, String aLogFileName, String log, String assignment, int iteration) {
//		logFileName = aLogFileName;
//		this.log=log;
//		this.assignment=assignment;
//		this.iteration=iteration;
//		isTests = anIsTests;
//	}
	
	public SendingData(LogEntryKind aLogEntryKind, String aLogFileName, String log, String assignment, int iteration) {
		logFileName = aLogFileName;
		this.log=log;
		this.assignment=assignment;
		this.iteration=iteration;
		logEntryKind = aLogEntryKind;
//		isTests = anIsTests;
	}
	
	public String getLogFileName() {
		return logFileName;
	}
	public LogEntryKind getLogEntryKind() {
		return logEntryKind;
	}
//	public boolean isTests() {
//		return isTests;
//	}
	
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
