package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors.FineGrained;

import java.text.ParseException;

import gradingTools.logs.bulkLogProcessing.collectors.EventCollectors.AbstractEventCollector;

public class TestRunsCollector extends AbstractEventCollector {

	private int lastRun=-1;
	
	public TestRunsCollector() {
		reqPass=1;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		
		int runNumber = Integer.parseInt(data[SESSION_NUMBER_INDEX]);
		if(runNumber>lastRun) {
			lastRun=runNumber;
			try {
				addActivity("TestRun",data[TIME_TESTED_INDEX]);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Error parsing date");
			}
		}
		
		
	}

	@Override
	public boolean requiresTestNames() {
		return false;
	}

	@Override
	public void reset() {
		super.reset();
		lastRun=-1;
	}
	
}
