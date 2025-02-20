package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class HangingHint5OnOutput extends HangingHint{

	static Class[] PREVIOUS_HINTS = {
			HangingHint3OnOutput.class
	};	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {	
		String aLine1 = "Thee are two blocking/unblocking conditions here";
		String aLine2 = "The first is a thread waiting to get a monitor lock held by another thread";
		String aLine3 = "The second is a thread waiting for another thread to terminate";
		String aLine4 = "Do you see the problem now?";

			
		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4;

	} 
	
	public static void main(String[] args)  {
		String aFullHint = new HangingHint5OnOutput().hint();
		System.out.println(aFullHint);
	}

}
