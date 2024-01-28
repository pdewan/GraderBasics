package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.MaxValue;
@MaxValue(0)
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
