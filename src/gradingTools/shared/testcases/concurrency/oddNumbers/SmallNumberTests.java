package gradingTools.shared.testcases.concurrency.oddNumbers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
	BasicsSmallProblem.class,
	ForkJoinSmallProblem.class,
	FairAllocationSmallProblem.class,
	SynchronizationSmallProblem.class,
	
})
@Explanation("The tests in this suite process the output of ConcurrentOddNumbers when it is run with the argument of 7, which results in the generation of a relatively small number of randoms")
public class SmallNumberTests {
	public static final String SMALL_EXPLANATION = "small problem.";

}
