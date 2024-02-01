package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class ForkJoinHint6OnCode extends ForkJoinHint{

	static Class[] PREVIOUS_HINTS = {
			ForkJoinHint5OnCode.class
	};

	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}
	@Override
	protected String hint() {		
		String aLine1 = "forkAndJoinThreads() uses one loop to do its tasks";
		String aLine2 = "You can change it to use multiple loops to do its tasks";

		return "\n" + aLine1 + "\n" + aLine2;		
	}

}
