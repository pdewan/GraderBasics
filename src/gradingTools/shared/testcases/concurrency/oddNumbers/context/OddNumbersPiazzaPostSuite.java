package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.BasicsLargerProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ForkJoinProblemPiazzaMessage.class,
	FairAllocationSmallProblemPiazzaMessage.class,
	FairAllocationLargerProblemPiazzaMessage.class,
	BasicsSmallProblemPiazzaMessage.class,
	BasicsLargerProblemPiazzaMessage.class,
	SynchronizationProblemPiazzaMessage.class
//	FairAllocationHints.class
//	FairAllocationHint1OnOutput.class,
//	FairAllocationHint2OnOutput.class,
//	FairAllocationHint3OnError.class,
//	FairAllocationHint5OnCode.class,
//	FairAllocationHint6OnCode.class		
})

public class OddNumbersPiazzaPostSuite extends ConcurrencySuiteSkeleton {
	public static void main (String[] args) {
		try {
			BasicJUnitUtils.interactiveTest(OddNumbersPiazzaPostSuite.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	static {
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//		setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
//	}
}

