package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(0)
@Explanation("This general secondary test runs ConcurrentOddNumbers with an argument of 7 and provides an analysis of the output used by the specific tests that determine how well you have fixed the individual bugs")
public class SmallNumberOfRandoms extends PreTestExecutorOfForkJoin {
	@Override
	protected int totalIterations() {
		return  7;
	}
    
}
