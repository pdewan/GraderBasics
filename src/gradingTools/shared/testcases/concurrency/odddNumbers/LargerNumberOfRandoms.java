package gradingTools.shared.testcases.concurrency.odddNumbers;

import util.annotations.MaxValue;
@MaxValue(10)
public class LargerNumberOfRandoms extends PreTestAbstractOddNumbersExecution {
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
