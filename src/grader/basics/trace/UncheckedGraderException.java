package grader.basics.trace;

import util.trace.UncheckedTraceableException;

public class UncheckedGraderException extends UncheckedTraceableException {
	public UncheckedGraderException(String aMessage) {
		super(aMessage);
	}
	public UncheckedGraderException() {
		super();
	}
	public UncheckedGraderException(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
	}

	

}
