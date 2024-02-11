package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint5OnThreadSwitch extends SynchronizationHint{

	static Class[] PREVIOUS_HINTS = {
			SynchronizationHint3OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		String aLine1 = "Look in the code for the sleep call";
		String aLine2 = "Before this line call a load occurs";
		String aLine3 = "Afer this line a save occurs";
		String aLine4 = "A sleep call results in a thread switch";
		String aLine5 = "What probem can occur if another thread executes between the load and save";


		
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
