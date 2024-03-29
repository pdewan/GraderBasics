package gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractStateChangeCollector;

public class IncreasingAttemptsCollector extends AbstractStateChangeCollector{
	
	public IncreasingAttemptsCollector() {
		this(" state has increased x times");
	}
	
	public IncreasingAttemptsCollector(String header) {
		super(header, "\\+");
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String [] passedTests = data[TEST_PASS_INDEX].split(" ");
		String [] partialTests = data[TEST_PARTIAL_INDEX].split(" ");
		check(passedTests,partialTests);
	}

}
