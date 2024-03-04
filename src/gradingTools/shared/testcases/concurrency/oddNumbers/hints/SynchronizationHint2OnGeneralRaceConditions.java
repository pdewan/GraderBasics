package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint2OnGeneralRaceConditions extends SynchronizationHint{

	static Class[] PREVIOUS_HINTS = {
			SynchronizationHint1OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		String aLine1 = "A race condition occurs when multiple threads attempt to execute the instructions of some method simultaneoulsy. ";
		String aLine2 = "Race conditions are undesirable when the method changes a data structure shared among the threads. ";
		String aLine3 = "Such a method is called a critical region or critical method. ";
		String aLine4 = "The reason for synchronizations error are race conditions. ";
		String aLine5 = "The error can be removed by declaring critical methods as synchronized. ";
		String aLine6 = "Examine the output to identify the race conditions behind the error. ";
		String aLine7 = "Examine the code to identify the methods that need to be synchronized. ";
		String aLine8 = "Declare the methods as synchronized.";
		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7 + "\n" + aLine8  ;

	}
	
//	@Override
//	protected String hint() {
//		String aLine1 = "A race condition occurs when multiple threads attempt to execute the instructions of some method simultaneoulsy";
//		String aLine2 = "Race conditions are undesirable when the method changes a data structure shared among the threads";
//		String aLine3 = "Such a method is called a critical region or critical method";
//		String aLine4 = "The reason for the synchronization error are race conditions";
//		String aLine5 = "The error can be removed by declaring critical methods as synchronized";
//		String aLine6 = "Examine the output to identify the race conditions behind the error";
//		String aLine7 = "Examine the output to identify the methods that need to be synchronized";
//		String aLine8 = "Declare the methods as synchronized";
//		String aLine9 = "Do not make non-critical methods synchronized";
//		String aLine10 = "A method always called by a synchronized method does not have to be made synchronized";
//		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7 + "\n" + aLine8 + "\n" + aLine9 + "\n" + aLine10;
//
//	}

}
