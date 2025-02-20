package gradingTools.shared.testcases.concurrency.oddNumbers;

import grader.basics.junit.TestCaseResult;

public abstract class PreTestExecutorOfForkJoin extends AbstractOddNumbersExecution {
	
	
	@Override
	public boolean isShowResult() {
		if (timeoutException != null) {
			return true;
		}
		return false;
	}

}
