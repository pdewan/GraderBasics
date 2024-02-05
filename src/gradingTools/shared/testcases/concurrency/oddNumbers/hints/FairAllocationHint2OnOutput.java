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
public class FairAllocationHint2OnOutput extends FairAllocationHint {
	static Class[] PREVIOUS_HINTS = {
			FairAllocationHint1OnOutput.class
	};

	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}

	@Override
	protected String hint() {
		// TODO Auto-generated method stub
		String aLine1 = "Look at the maximum and minimum number of items allocated to each thread";
		String aLine2 = "For each item allocated, a loop iteration is performed by a worker thread to determine if it is odd";
		String aLine3 = "The fair allocation test reports the max and min iterations performed";
		String aLine4 = "The difference between these two values should be 0 if the total number divides evenly among the threads";
		String aLine5 = "The difference should be 1 if it does not.";
		String aLine6 = "So the difference should be <= 1";
		return "\n" + aLine1 + "\n" + aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6;		   
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
