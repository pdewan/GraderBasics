package grader.basics.execution;

import java.util.List;

import util.trace.TraceableError;

public class EntryPointNotFound extends TraceableError{

	public EntryPointNotFound(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
	}
	
	public static EntryPointNotFound newCase (Object aFinder, List<String> aBasicCommand) {
		String aMessage = "Entry point not found in command:" + aBasicCommand + ". Should command require entry tag?";
		EntryPointNotFound retVal = new EntryPointNotFound(aMessage, aFinder);
		retVal.announce();
		return retVal;
	}

}
