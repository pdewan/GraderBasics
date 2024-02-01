package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class ForkJoinHint3OnOutput extends ForkJoinHint{

	static Class[] PREVIOUS_HINTS = {
			ForkJoinHint2OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}
	@Override
	protected String hint() {
		
		String aLine1 = "When the main thread calls Thread.start(), the follwing string is output:";
		String aLine2 = "\"Starting:Thread\"";
		String aLine3 = "When the main thread calls Thread.join(), the following string is output:";
		String aLine4 = "\"Stopping execution until the following thread terminates\"";
		String aLine5 = "When the forked thread starts is run() method, the following string is output";
		String aLine6 = "\"run() called to start processing subsequence\"";
		String aLine7 = "When the forked thread ends its run() method, the following string is output";
		String aLine8 = "\"run() terminates to end processing of subsequence\"";
		String aLine9 = "When the main thread resumes execution after Thread.join(), the following string is output:";
		String aLine10 = "Look at the order in which these strings in the output";
		String aLine11 = "You need to change the code in forkAndJoinThreads() to change this order";
		return "\n" + aLine1 + "\n" + aLine2+ "\n" + aLine3 + "\n" + aLine4 + 
				"\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7 + "\n" + aLine8 + "\n" + aLine9 + "\n" + aLine10 + "\n" + aLine11;
		
	}

}
