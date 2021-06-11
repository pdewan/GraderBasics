package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public class DateFirstTestedCollector extends AbstractCollector {
	
	private final String headerPhrase;
	
	public DateFirstTestedCollector() {
		this("First Date Test");
	}
	
	public DateFirstTestedCollector(String header) {
		headerPhrase=header;
		reqPass=1;
		results = new String[1];
		results[0]=null;
		generateHeaders();
	}
	
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		if(results[0]==null)
			results[0]=data[TIME_TESTED_INDEX];
	}

	@Override
	public String[] getResults() {
		return results;
	}
	
	@Override
	public void reset() {
		results[0]=null;
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}

	@Override
	protected String getHeaderPhrase() {
		return headerPhrase;
	}
	
	@Override
	protected void generateHeaders() {
		headers = new String[1];
		headers[0]=getHeaderPhrase();
	}
	
}
