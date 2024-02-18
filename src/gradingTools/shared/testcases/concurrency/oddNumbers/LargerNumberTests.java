package gradingTools.shared.testcases.concurrency.oddNumbers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import util.annotations.Explanation;
@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
	BasicsLargerProblem.class,
	ForkJoinLargerProblem.class,
	FairAllocationLargerProblem.class,
	SynchronizationLargerProblem.class,
	
})
@Explanation("The tests in this suite process the output of ConcurrentOddNumbers when it is run with the argument of 14, which results in the generation of a relatively larger number of randoms in compariosn to the SmallNumberTests suite")
public class LargerNumberTests extends PreTestExecutorOfForkJoin {
	public static final String LARGER_EXPLANATION = "larger problem.";

//	Class[] PRECEDING_TESTS = {
//			SmallNumberOfRandoms.class,
//	};
//	@Override
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS ;
//	}
//	@Override
//	protected int totalIterations() {
//		return  14;
//	}
    
}
