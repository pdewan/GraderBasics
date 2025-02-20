package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class HangingHint2OnOutput extends HangingHint{

	static Class[] PREVIOUS_HINTS = {
			HangingHint1OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		String aLine1 = "Recall what happens when a thread tries to enter a synchronized method";
		String aLine2 = "Read the comments above the class BasicSynchronizationDemo if necessary";		
		String aLine3 = "Identify attempts to call a method that do not result in the method being entered";
		String aLine4 = "Identify the threads that make such attempts.";
		String aLine5 = "Do you see the reason for the program hanging";		
		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 ;

	}
	
	public static void main(String[] args)  {
		String aFullHint = new HangingHint2OnOutput().hint();
		System.out.println(aFullHint);
	}

}
