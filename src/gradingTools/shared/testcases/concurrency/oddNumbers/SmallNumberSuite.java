package gradingTools.shared.testcases.concurrency.oddNumbers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SmallNumberOfRandoms.class,
	BasicsSmallProblem.class,
	ForkJoinSmallProblem.class,
	FairAllocationSmallProblem.class,
	SynchronizationSmallProblem.class,
	
})

public class SmallNumberSuite {

}
