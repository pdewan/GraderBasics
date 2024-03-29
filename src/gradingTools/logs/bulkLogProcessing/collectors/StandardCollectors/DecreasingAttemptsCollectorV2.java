package gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors;

import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractStateChangeCollector;

public class DecreasingAttemptsCollectorV2 extends AbstractStateChangeCollector{
	
	public DecreasingAttemptsCollectorV2() {
		this(" state has decreased x times");
	}
	
	public DecreasingAttemptsCollectorV2(String header) {
		super(header,"");
	}


	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		updateTests(data);
		
		List<String> movedTests = getReducedTests();
		String [] declinedTests = new String[movedTests.size()];
		movedTests.toArray(declinedTests);
		if(declinedTests.length>0)
			check(declinedTests);
	}

	@Override
	public void reset() {
		super.reset();
	}
	
}
