package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class HangingHint1OnOutput extends HangingHint{

	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return noPreviousHints();
	}

	@Override
	protected String hint() {
		String aLine1 = "Look at the program output from the start -  there will not be much of it";
		String aLine2 = "If you ran the program through a test, then stop looking at the text after the time out exception:";
		String aLine3 = "\"java.util.concurrent.TimeoutException\"";
		String aLine4 = "Study the messages that indicate calling, entering and exiting of methods";
		String aLine5 = "Study the threads that output these messages";
		String aLine6 = "Do you see the reason for the program hanging";
		
		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6;

	}

}
