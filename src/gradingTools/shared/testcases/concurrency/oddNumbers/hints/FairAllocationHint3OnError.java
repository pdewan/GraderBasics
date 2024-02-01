package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.AnAbstractTestLogFileWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import util.annotations.MaxValue;
@MaxValue(0)
public class FairAllocationHint3OnError extends FairAllocationHint {
	static Class[] PREVIOUS_HINTS = {
			FairAllocationHint2OnOutput.class
	};
//	static Class[] PRECEDING_TESTS = {
//			FairAllocationSmallProblem.class
//	};
//	
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		// TODO Auto-generated method stub
		String aLine1 = "Look at the error message produced by FairAllocationSmallProblem and FairAllocationKLargerProblem";
		String aLine2 = "There are two possible errors reported";
		String aLine3 = "The sum of the total items allocated to each thread runnable is not the same as the total number of items in the list";
		String aLine4 = "So the difference between the min and max allocations is not <= 1";
		String aLine5 = "You need to fix one or both errors";

		return "\n" + aLine1 + "\n" + aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5;		   
	}
	
	
//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {
//		AnAbstractTestLogFileWriter[] aFileWriters =
//				TestLogFileWriterFactory.getFileWriter();
//		AnAbstractTestLogFileWriter aFineGrainedWriter = aFileWriters[1];
//		
//		String aPreviousContents = aFineGrainedWriter.getPreviousContents();
//		String aName = FairAllocationSmallProblem.class.getSimpleName();
//		boolean hasBeenRun = aPreviousContents.contains(aName);
//		
////		PassFailJUnitTestCase aTestCase = JUnitTestsEnvironment
////		.getAndPossiblyRunGradableJUnitTest(FairAllocationSmallProblem.class);
//		return null;
//	}

}
