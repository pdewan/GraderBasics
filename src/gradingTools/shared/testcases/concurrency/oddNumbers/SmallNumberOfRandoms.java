package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.MaxValue;
@MaxValue(15)
public class SmallNumberOfRandoms extends PreTestAbstractOddNumbersExecution {
	@Override
	protected int totalIterations() {
		return  7;
	}
    
}
