package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import util.annotations.Explanation;

@Explanation("This hint identifies parts of the output trace and comments on the semantics of relevant Java Thread methods to help fix the fork-join bug")
public class ForkJoinHint2OnCodeAndOutput extends ForkJoinHint{

	static Class[] PREVIOUS_HINTS = {
			ForkJoinHint1OnError.class
	};

	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}
	@Override
	protected String hint() {
		String aLine1 = "Understand what the Thread.start() call does by looking at the comment accompanying the call if necessary. ";
		String aLine2 = "Understand what the Thread.join() call does by looking at the comment accompanying the call if necessary. ";
		String aLine3 = "Understand when the run() method of OddNumberWorkerCode is called by looking at the comments accompnaying it. ";
		String aLine4 = "Look at the order of \"(TS)\" \"(SJ)|\" and \"(EJ)\" lines in the output. "; 
		String aLine5 = "You need to fix the code in forkAndJoinThreads() to change this order. "; 	
		return "\n" + aLine1 + "\n" + aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5;
	}

}
