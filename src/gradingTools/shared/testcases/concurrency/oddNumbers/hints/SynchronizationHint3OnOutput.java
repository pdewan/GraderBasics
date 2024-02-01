package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint3OnOutput extends SynchronizationHint{

	static Class[] PREVIOUS_HINTS = {
			SynchronizationHint2OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		String aLine1 = "To identify the race conditions, look in the output for (a) multiple increments that are reduced to one increment and (b) multiple list adds that are reduced to one add";
		String aLine2 = "To identify the associated citical regions or methods, look for output lines containing the string:";
		String aLine3 = "\"Entered static method\" or \"Entered instance method:";
			
		return "\n" + aLine1 +  "\n" + aLine2 + "\n" + aLine3 + "\n" ;

	}

}
