package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.hanging.HangingScenario;
import util.annotations.MaxValue;
@MaxValue(0)
public abstract class HangingHint extends AbstractHint {
	
	static Class[] PRECEDING_TESTS = {
			HangingScenario.class,
//			FairAllocationLargerProblem.class
	};
	
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
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
