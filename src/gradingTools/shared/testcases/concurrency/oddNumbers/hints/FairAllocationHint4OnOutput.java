package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(0)
@Explanation("This hint identifies additional trace lines to look at to help identify the natire of the fair-allocation problem")
public class FairAllocationHint4OnOutput extends FairAllocationHint {
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
		String aLine1 = "Look at the output lines containing the strings \"(MA)\" and \"(RA)\"";
		String aLine2 = "Lines with \"(MA)\" indicate the mininimum number of items allocated to each thread";
		String aLine3 = "They are output by calls to the predefined method, traceMinimumAllocation()";
		String aLine4 = "Lines with \"(RA)\" indicate per-therad allocation of the remainder of items after the mimimum allocation has been allocated to each thread";
		String aLine5 = "For each of the four threads in the small problem try to determine the fair minimum and remaninder allocation";
		String aLine6 = "Now try to determine a general scheme for allocating N work items to M threads";
		String aLine7 = "that works for both the small and larger problem in particular, and arbitrary values of N amd M in general";
		return "\n" + aLine1 + "\n" + aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5 + "\n" + aLine6 + "\n" + aLine7;		   
	}
	
	
//	@Override i
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
