package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import gradingTools.logs.localChecksStatistics.collectors.AbstractStateChangeCollector;

public class IncreasingAttemptsCollector extends AbstractStateChangeCollector{
	
	public IncreasingAttemptsCollector() {
		this(" increases");
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
