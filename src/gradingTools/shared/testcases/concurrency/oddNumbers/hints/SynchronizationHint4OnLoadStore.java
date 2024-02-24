package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint4OnLoadStore extends SynchronizationHint{

	static Class[] PREVIOUS_HINTS = {
			SynchronizationHint3OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {		
		String aLine1 = "To identify relevant trace output, look in the output for lines containing the strings:";
		String aLine2 = "\"(LD)\" and \"(SV)\"";
		String aLine3 = "To identify the method outputing these lines, \n look in the code for the strings \"traceLoad\" and \"traceSave\" ";
		
//		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";

			
		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3;

	}
	
//	@Override
//	protected String hint() {
//		String aLine1 = "To identify the race conditions, look for output containing the strings:";
//		String aLine2 = "\"Loaded total number of odd numbers\" and \"Saved total number of odd numbers\"";
//		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";
//
//			
//		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3;
//
//	}

}
