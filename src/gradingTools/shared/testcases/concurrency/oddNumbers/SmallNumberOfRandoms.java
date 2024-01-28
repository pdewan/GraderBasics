package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.MaxValue;
@MaxValue(0)
public class SmallNumberOfRandoms extends PreTestExecutorOfForkJoin {
	@Override
	protected int totalIterations() {
		return  7;
	}
    
}
