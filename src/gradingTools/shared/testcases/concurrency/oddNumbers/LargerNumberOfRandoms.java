package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(0)
@Explanation("This general secondary test runs ConcurrentOddNumbers on a larger problem, created by giving an argument of 14 to the main method, and provides an analysis of the output used by the specific tests that determine how well you have fixed the individual bugs")
public class LargerNumberOfRandoms extends PreTestExecutorOfForkJoin {
//	Class[] PRECEDING_TESTS = {
//			SmallNumberOfRandoms.class,
//	};
//	@Override
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS ;
//	}
	@Override
	protected int totalIterations() {
		return  14;
	}
    
}
