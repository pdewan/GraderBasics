package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class ForkJoinHint5OnCode extends ForkJoinHint{

	static Class[] PREVIOUS_HINTS = {
			ForkJoinHint4OnCode.class
	};

	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}
	@Override
	protected String hint() {		
		String aLine1 = "forkAndJoinThreads() starts a thread, then waits for it to finish, then starts the next thread, waits for it to finish, and so on ...";
		String aLine2 = "Do you see why this results in the threads executing one after the other";
		String aLine3 = "You need to change the order to ensure that forked threads are created and executed concurrently";

		return "\n" + aLine1 + "\n" + aLine2 + "\n" + aLine3;		
	}

}
