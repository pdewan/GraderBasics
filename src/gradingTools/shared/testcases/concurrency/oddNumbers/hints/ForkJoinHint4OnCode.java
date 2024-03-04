package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import util.annotations.Explanation;

@Explanation("This hint identifies specific aspects of the fork-join code in forkAndJoinThreads() to help identify the fork-join bug")
public class ForkJoinHint4OnCode extends ForkJoinHint{

	static Class[] PREVIOUS_HINTS = {
			ForkJoinHint3OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}
	@Override
	protected String hint() {		
		String aLine1 = "Look at the sequence of forkThread() and joinThread() calls that are executed by the current version of forkAndJoinThreads(). ";
		String aLine2 = "You need to change forkAndJoinThreds() to make these calls in a different order.";
		return "\n" + aLine1 + "\n" + aLine2;		
	}	

}
