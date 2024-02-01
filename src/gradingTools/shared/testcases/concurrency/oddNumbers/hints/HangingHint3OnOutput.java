package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class HangingHint3OnOutput extends HangingHint{

	static Class[] PREVIOUS_HINTS = {
			HangingHint2OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {	
		String aLine1 = "Identify methods that are entered but not exited";
		String aLine2 = "Identify the threads that have called these methods";
		String aLine3 = "These threads must be waiting for some condition that prevents them from exiting";
		String aLine4 = "What are these conditions?";		
		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4;

	}

}
