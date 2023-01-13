package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

import java.text.ParseException;
import java.util.List;

public class TestAttemptTimeEvent extends AbstractEventCollector{

	public TestAttemptTimeEvent() {
		reqPass=1;
	}
	private final String [] set_headers = {"case_id,timestamp_start,timestamp_end,activity,user"};
	private String lastTest=null;
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {

		updateTests(data);
		List<String> movedTests = getChangedTests();
		
		if(lastTest==null) {
			lastTest = data[TIME_TESTED_INDEX];
		}
		
		StringBuilder tests = new StringBuilder();
		for(String test:movedTests) {
			tests.append(test+" ");
		}
		
		if(movedTests.size()>0) {
			
			
			try {
				addActivity("\""+tests.toString()+"\"",lastTest,data[TIME_TESTED_INDEX]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			lastTest = data[TIME_TESTED_INDEX];
		}
		
	}

	@Override
	public void reset() {
		super.reset();
		lastTest=null;
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}

	@Override
	public boolean requiresSuiteMapping() {
		return false;
	}

	@Override
	public String[] getHeaders() {
		eventCount=0;
		return set_headers;
	}
	
	protected void addActivity(String activity, String dateStart, String dateEnd) throws ParseException {
		String case_id = Integer.toString(eventCount);
		eventCount++;
		workingResults.add(case_id+","+convertDate(dateStart)+","+ convertDate(dateEnd) +","+activity+","+studentName+"\n");
	}
	
}
