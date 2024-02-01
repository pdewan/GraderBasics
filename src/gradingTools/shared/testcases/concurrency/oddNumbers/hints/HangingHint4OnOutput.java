package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class HangingHint4OnOutput extends HangingHint{

	static Class[] PREVIOUS_HINTS = {
			HangingHint3OnOutput.class
	};	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {	
		String aLine1 = "There would be no hanging in this problem if only one thread was waiting for its unblocking condition";
		String aLine2 = "The problem is that multiple threads are waiting for their unblocking conditions";
			
		return "\n" + aLine1 + "\n" +aLine2;

	}

}
