package grader.basics.junit;

import grader.basics.trace.UncheckedGraderException;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 10/5/13
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotAutomatableException extends UncheckedGraderException {
	public NotAutomatableException(String aMessage) {
		super(aMessage);
	}
	
	public NotAutomatableException(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
		// TODO Auto-generated constructor stub
	}

	public NotAutomatableException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static NotAutomatableException newCase(String aMessage, Object aFinder) {
		NotAutomatableException retVal = new NotAutomatableException(aMessage,  aFinder);
		retVal.announce();		
		return retVal;
	}
//	public static void main (String[] args) {
//		Tracer.showInfo(true);
//		Tracer.setKeywordPrintStatus(Tracer.ALL_KEYWORDS, true);
//		newCase("", null);
//	}
}
