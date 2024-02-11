package gradingTools.shared.testcases.concurrency.oddNumbers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SmallNumberOfRandoms.class,
	LargerNumberOfRandoms.class,
//	BasicsSmallProblem.class,
//	ForkJoinSmallProblem.class,
//	FairAllocationSmallProblem.class,
//	SynchronizationSmallProblem.class,
	
})
@Explanation ("These are general secondary tests on which the specific primary tests to detect individual bugs depend. You do not have to run them directly. They are run automatically by the primary tests")
public class HelperTestsOddNumbers {

}
