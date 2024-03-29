package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class SynchronizationHint1OnError extends SynchronizationHint{

	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return noPreviousHints();
	}

	@Override
	protected String hint() {
		String aLine1 = "Each time an odd number is found by a worker thread, a line with the following string is output:";
		String aLine2 = "\"Is Odd:true\". ";
		String aLine3 = "Also the total number of odd numbers kept by the program is incremented when an odd number is found. ";
		String aLine4 =  "When all odd numbers have been found, the total number of odd numbers computed is printed in a line with the following string:";
		String aLine5 = "\"Total Num Odd Numbers:\". ";
		String aLine6 = "Sum up the lines indicating that an odd number was found, which is equal to the number of increments to Total Num Odd Numbers. ";
		String aLine7 = "If the sum is not the same as Total Num Odd Numbers. ";
		String aLine8 = "look at the code and next four hints to understand why this may have happened. ";
		String aLine9 = "If the sum is correct, look at the last hint on shared variables and critical code. ";


		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6 + 
				"\n" + aLine7 + "\n" + aLine8 + "\n" + aLine9;

	}
	
//	@Override
//	protected String hint() {
//		String aLine1 = "Each time an odd number is found by a worker thread, a line with the following string is output:";
//		String aLine2 = "\"Is Odd:true\"";
//		String aLine3 = "Also the total number of odd numbers kept by the program is incremented when an odd number is found";
//		String aLine4 =  "When all odd numbers have been found, the total number of odd numbers computed is printed in a line with the following string:";
//		String aLine5 = "\"Total Num Odd Numbers:\"";
//		String aLine6 = "Sum up the lines indicating that an odd number was found, which is equal to the number of increments to Total Num Odd Numbers";
//		String aLine7 = "This sum is not the same as Total Num Odd Numbers";
//
//		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7;
//
//	}

}
