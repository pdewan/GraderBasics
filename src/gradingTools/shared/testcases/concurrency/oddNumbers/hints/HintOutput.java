package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

public class HintOutput {
	public static void main (String[] args) {
		System.out.println(hint5());
	}
	protected static  String hint1() {
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
	
	protected static String hint2() {
		String aLine1 = "A race condition occurs when multiple threads attempt to execute the instructions of some method simultaneously. ";
		String aLine2 = "Race conditions are undesirable when the method changes a data structure shared among the threads. ";
		String aLine3 = "Such a method is called a critical region or critical method. ";
		String aLine4 = "The reason for synchronizations error are race conditions. ";
		String aLine5 = "The error can be removed by declaring critical methods as synchronized. ";
		String aLine6 = "Examine the output to identify the race conditions behind the error. ";
		String aLine7 = "Examine the code to identify the methods that need to be synchronized. ";
		String aLine8 = "Declare the methods as synchronized.";
		return "\n" + aLine1 + "\n" +aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7 + "\n" + aLine8  ;

	}
	protected static String hint3() {
		String aLine1 = "To identify the race conditions, look in the output for ";
		String aLine2 = "multiple increments that are reduced to one increment ";		
//		String aLine2 = "To identify the associated citical regions or methods, look for output lines containing the string:";
//		String aLine3 = "\"Entered static method\" or \"Entered instance method:";
//			
		return "\n" + aLine1 +  "\n" + aLine2;

	}
	
	protected static String hint4() {		
		String aLine1 = "To identify relevant trace output, look in the output for lines containing the strings:";
		String aLine2 = "\"(LD)\" and \"(SV)\". ";
		String aLine3 = "To identify the method outputing these lines, \n look in the code for the strings \"traceLoad\" and \"traceSave\" ";
		
//		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";

			
		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3;

	}
	
	protected static String hint5() {
		String aLine1 = "Look in the code for the sleep call. ";
		String aLine2 = "Before this line call a load occurs. ";
		String aLine3 = "After this line a save occurs. ";
		String aLine4 = "A sleep call results in a thread switch.";
		String aLine5 = "What problem can occur if another thread executes between the load and save? ";


		
//		String aLine3 = "\"Adding odd number to all odd numbers\" and \"All Odd Numbers\"";

			
		return "\n" + aLine1 + "\n" +aLine2  + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5;
	}

}
