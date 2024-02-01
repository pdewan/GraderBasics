package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint4OnOutput extends SynchronizationHint{

	static Class[] PREVIOUS_HINTS = {
			SynchronizationHint3OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		String aLine1 = "To identify the race conditions, look for output containing the strings:";
		String aLine2 = "\"Loaded total number of odd numbers\" and \"Saved total number of odd numbers\"";
		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";

			
		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3;

	}

}
