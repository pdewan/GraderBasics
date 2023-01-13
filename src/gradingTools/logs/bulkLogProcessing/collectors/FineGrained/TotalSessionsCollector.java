package gradingTools.logs.bulkLogProcessing.collectors.FineGrained;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class TotalSessionsCollector extends AbstractCollector {

	private final String headerPhrase;
	
	public TotalSessionsCollector() {
		this( "Number of Sessions");
	}
		
	public TotalSessionsCollector(String header) {
			headerPhrase=header;
			reqPass=1;
			results = new String[1];
			results[0]=null;
			generateHeaders();
	}
	
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		results[0]=data[SESSION_NUMBER_INDEX];
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
