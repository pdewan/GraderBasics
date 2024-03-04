package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(0)
@Explanation("This hint identifies parts of the output trace to compare to help fix the task-allocation bug in fairThreadRemainderSize()")
public class FairAllocationHint1OnOutput extends FairAllocationHint {
	
//	static Class[] PRECEDING_TESTS = {
//			FairAllocationSmallProblem.class
//	};
//	
//	protected Class[] precedingTests() {"
//		return PRECEDING_TESTS;
//	}
	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return noPreviousHints();
	}

	@Override
	protected String hint() {
		// TODO Auto-generated method stub
		String aLine1 = "Look at the console outputs indicating how much of the input list is allocated to each thread runnable. ";
		String aLine2 = "To do so search the output for lines with the following string:";
		String aLine3 = "\"run() called to start processing subsequence\". ";
		String aLine4 = "Compare the number of item allocated to each thread runnable.";
		return "\n" + aLine1 + "\n" + aLine2 + "\n" + aLine3 + "\n" + aLine4;		   
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
