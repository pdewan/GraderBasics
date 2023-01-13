package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

import java.text.ParseException;
import java.util.List;

public class TestWorkingSetEvent extends AbstractEventCollector{

	private final String [] testName;

	public TestWorkingSetEvent(String ... testName) {
		reqPass=1;
		this.testName=testName;
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException {

		updateTests(data);
		List<String> movedTests = getChangedTests();
		
		for(String test:movedTests) {
			if(!contains(testName, test)) continue;
			try {
				addActivity("\""+movedTests.toString()+"\","+movedTests.size()+",",data[TIME_TESTED_INDEX]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return;
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
