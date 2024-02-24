package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import util.annotations.Explanation;

@Explanation("This hint in identifies specific aspects of the relevant trace output to help identify the fork-join bug")
public class ForkJoinHint3OnOutput extends ForkJoinHint{

	static Class[] PREVIOUS_HINTS = {
			ForkJoinHint2OnCodeAndOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}
	@Override
	protected String hint() {
		
		String aLine1 = "When the main thread calls Thread.start(), a line with the follwing string is output:";
		String aLine2 = "\"(TS)\"";
		String aLine3 = "When the main thread starts the blocking Thread.join(), a line with following string is output:";
		String aLine4 = "\"(SJ)\"";		
		String aLine5 = "When the forked thread starts its run() method, a line with the following string is output";
		String aLine6 = "\"(EN) run()\"";
		String aLine7 = "When the forked thread ends its run() method, the following string is output";
		String aLine8 = "\"(EX) Exiting method:run\"";
		String aLine9 = "When the main thread resumes execution after Thread.join(), a line with the following string is output:";
		String aLine10 = "\"(EJ)\"";
		String aLine11 = "Look at the order of these strings in the output";
		String aLine12= "You need to change the code in forkAndJoinThreads() to change this order";
		return "\n" + aLine1 + "\n" + aLine2+ "\n" + aLine3 + "\n" + aLine4 + 
				"\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7 + "\n" + aLine8 + "\n" +
				aLine9 + "\n" + aLine10 + "\n" + aLine11 + "\n" + aLine12;
		
	}
	
//	
//	@Override
//	protected String hint() {
//		
//		String aLine1 = "When the main thread calls Thread.start(), the follwing string is output:";
//		String aLine2 = "\"Starting:Thread\"";
//		String aLine3 = "When the main thread calls Thread.join(), the following string is output:";
//		String aLine4 = "\"Stopping execution until the following thread terminates\"";
//		String aLine5 = "When the forked thread starts is run() method, the following string is output";
//		String aLine6 = "\"run() called to start processing subsequence\"";
//		String aLine7 = "When the forked thread ends its run() method, the following string is output";
//		String aLine8 = "\"run() terminates to end processing of subsequence\"";
//		String aLine9 = "When the main thread resumes execution after Thread.join(), the following string is output:";
//		String aLine10 = "Look at the order of these strings in the output";
//		String aLine11 = "You need to change the code in forkAndJoinThreads() to change this order";
//		return "\n" + aLine1 + "\n" + aLine2+ "\n" + aLine3 + "\n" + aLine4 + 
//				"\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7 + "\n" + aLine8 + "\n" + aLine9 + "\n" + aLine10 + "\n" + aLine11;
//		
//	}

}
