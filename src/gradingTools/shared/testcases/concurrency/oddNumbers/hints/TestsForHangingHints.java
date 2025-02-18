package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.BasicsLargerProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.BasicsSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinLargerProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.LargerNumberOfRandoms;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationLargerProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.hanging.HangingScenario;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	BasicsLargerProblem.class,
	LargerNumberOfRandoms.class,
	HangingScenario.class,
//	SynchronizationHints.class
//	SynchronizationHint1OnOutput.class,
//	SynchronizationHint2OnOutput.class,
//	SynchronizationHint3OnOutput.class,
//	SynchronizationHint4OnOutput.class,		
})

public class TestsForHangingHints extends ConcurrencySuiteSkeleton {
	public static void main (String[] args) {
		try {
			
			BasicJUnitUtils.interactiveTest(TestsForHangingHints.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
