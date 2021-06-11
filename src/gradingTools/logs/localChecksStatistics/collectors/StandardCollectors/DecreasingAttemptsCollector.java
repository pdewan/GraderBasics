package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import gradingTools.logs.localChecksStatistics.collectors.AbstractStateChangeCollector;

public class DecreasingAttemptsCollector extends AbstractStateChangeCollector{
	
	public DecreasingAttemptsCollector() {
		this(" state has decreased x times");
	}
	
	public DecreasingAttemptsCollector(String header) {
		super(header, "-");
	}

	private boolean isFirst=true;
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		if(isFirst) {
			isFirst=false;
			return;
		}
		
		String [] failedTests = data[TEST_FAIL_INDEX].split(" ");
		String [] partialTests = data[TEST_PARTIAL_INDEX].split(" ");
		check(failedTests,partialTests);
	}

	@Override
	public void reset() {
		isFirst=true;
		super.reset();
	}
	
}
