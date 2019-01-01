package grader.basics.execution;

import util.trace.TraceableError;

public class EntryPointNotFound extends TraceableError{

	public EntryPointNotFound(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
	}
	
	public static EntryPointNotFound newCase (Object aFinder) {
		String aMessage = "Entry point not found";
		EntryPointNotFound retVal = new EntryPointNotFound(aMessage, aFinder);
		retVal.announce();
		return retVal;
	}

}
