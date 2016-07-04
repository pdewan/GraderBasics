package grader.basics.execution;

import grader.basics.trace.UncheckedGraderException;

/**
 * This exception means that the student's program failed to run
 */
public class NotRunnableException extends  UncheckedGraderException {
	public NotRunnableException(String aMessage) {
		super(aMessage);
	}
	
	public NotRunnableException(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
		// TODO Auto-generated constructor stub
	}

	public NotRunnableException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static NotRunnableException newCase(String aMessage, Object aFinder) {
		NotRunnableException retVal = new NotRunnableException(aMessage,  aFinder);
		retVal.announce();		
		return retVal;
	}
}