package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

import java.text.ParseException;
import java.util.Map;

import gradingTools.logs.bulkLogProcessing.tools.dataStorage.TestState;

public class TestStatusEvent extends AbstractEventCollector{
	
	public TestStatusEvent() {
		reqPass=1;
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		updateTests(data);
		try {
			Map<String,TestState> testMap = getTestStates();
			for(String test:getChangedTests()) {
				TestState testState = testMap.get(test);
				addActivity(test+"_"+testState, data[TIME_TESTED_INDEX]);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Error parsing date");
		}
	}

	@Override
	public void reset() {
		super.reset();
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}

	@Override
	public boolean requiresSuiteMapping() {
		return false;
	}

}
