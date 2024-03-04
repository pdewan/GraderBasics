package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import util.annotations.Explanation;

@Explanation("This hint explains how to interpret the error message from the test for fork-join bug")
public class ForkJoinHint1OnError extends ForkJoinHint{

	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return noPreviousHints();
	}

	@Override
	protected String hint() {
		String aLine1 = "The error message from the fork join test indicates no interleaving. ";
		String aLine2 = "This means that between the first and last output of each forked thread, there is no other thread output. ";
		String aLine3 = "Each thread's output starts after the previous thread has finished its task and stopped output";
		String aLine4 = "Thus the forked threads do not execute concurrently. ";
		String aLine5 = "You need to change forkAndJoinThreads() to fix this problem. ";
		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5;

	}

}
