package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint6OnSharedVariablesAndCriticalCode extends SynchronizationHint{

	static Class[] PREVIOUS_HINTS = {
			SynchronizationHint3OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		String aLine1 = "Any code that changes a shared variable should be critical code. ";
		String aLine2 = "To find all critical code, look in the output for the string \" shared \":";
		String aLine3 = "To find the methods changing them, look in the code for the strings:";
		String aLine4 = "\"traceLoad\", \"traceList\". ";
		String aLine5 = "Look in the assignment description about Synchronization Theory to understand how to create critical codei in Java. ";

//		String aLine10 = "A method always called by a synchronized method does not have to be made synchronized as the calling method has already acquired the lock";

//		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";

			
		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3 + "\n" + aLine4;

	}

}
