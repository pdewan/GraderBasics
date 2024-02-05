package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblem;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SmallNumberOfRandoms.class,
	SynchronizationSmallProblem.class,
	ForkJoinSmallProblem.class,
//	SynchronizationHints.class
//	SynchronizationHint1OnOutput.class,
//	SynchronizationHint2OnOutput.class,
//	SynchronizationHint3OnOutput.class,
//	SynchronizationHint4OnOutput.class,		
})

public class TestsForSynchronizationHints extends ConcurrencySuiteSkeleton {
	public static void main (String[] args) {
		try {
			
			BasicJUnitUtils.interactiveTest(TestsForSynchronizationHints.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
