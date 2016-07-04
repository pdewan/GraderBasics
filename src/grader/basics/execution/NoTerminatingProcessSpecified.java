package grader.basics.execution;

import util.trace.TraceableError;

public class NoTerminatingProcessSpecified extends TraceableError{

	public NoTerminatingProcessSpecified(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
	}
	
	public static NoTerminatingProcessSpecified newCase (Object aFinder) {
		String aMessage = "Entry point not found";
		NoTerminatingProcessSpecified retVal = new NoTerminatingProcessSpecified(aMessage, aFinder);
		retVal.announce();
		return retVal;
	}

}
