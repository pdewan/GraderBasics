package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestsForFairAllocationHints.class,
	FairAllocationHints.class
//	FairAllocationHint1OnOutput.class,
//	FairAllocationHint2OnOutput.class,
//	FairAllocationHint3OnError.class,
//	FairAllocationHint5OnCode.class,
//	FairAllocationHint6OnCode.class		
})

public class FairAllocationHintsSuite extends ConcurrencySuiteSkeleton {
	public static void main (String[] args) {
		try {
			BasicJUnitUtils.interactiveTest(FairAllocationHintsSuite.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
		setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
	}
}

