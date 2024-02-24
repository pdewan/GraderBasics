package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint2OnOutputAndAssociatedCode extends SynchronizationHint{

	static Class[] PREVIOUS_HINTS = {
			SynchronizationHint3OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
//		String aLine1 = "Each time an odd number is found by a worker thread, a line with the following string is output:";
//		String aLine2 = "\"Is Odd:true\"";
//		String aLine3 = "Also the total number of odd numbers kept by the program is incremented when an odd number is found";
//		String aLine4 =  "When all odd numbers have been found, the total number of odd numbers computed is printed in a line with the following string:";
//		String aLine5 = "\"Total Num Odd Numbers:\"";
//		String aLine6 = "Sum up the lines indicating that an odd number was found, which is equal to the number of increments to Total Num Odd Numbers";
//		String aLine7 = "This sum is not the same as Total Num Odd Numbers";
//		String aLine8 = "Look at the code to understand why this may have happened";
		String aLine1 = "To identify relevant trace output, look in the output for lines containing the strings:";
		String aLine2 = "\"(LD)\" and \"(SV)\"";
		String aLine3 = "To identify the method outputing these lines, \n look in the code for the strings \"traceLoad\" and \"traceSave\" ";
		
//		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";

			
		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3;

	}
	
//	@Override
//	protected String hint() {
//		String aLine1 = "To identify the race conditions, look for output containing the strings:";
//		String aLine2 = "\"Loaded total number of odd numbers\" and \"Saved total number of odd numbers\"";
//		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";
//
//			
//		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3;
//
//	}

}
