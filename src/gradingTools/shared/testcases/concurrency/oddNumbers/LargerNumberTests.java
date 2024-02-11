package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(0)
@Explanation("The tests in this suite process the output of ConcurrentOddNumbers when it is run with the argument of 14, which results in the generation of a relatively larger number of randoms in compariosn to the SmallNumberTests suite")
public class LargerNumberTests extends PreTestExecutorOfForkJoin {
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
