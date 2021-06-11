package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public class TotalAttemptsCollector extends AbstractCollector {

	private int count;
	private final String headerPhrase;
	
	public TotalAttemptsCollector() {
		this("Number of Times Tested");

	}
	
	public TotalAttemptsCollector(String header) {
		headerPhrase=header;
		reqPass=1;
		count=0;
		results = new String [1];
		generateHeaders();
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		count++;
	}

	@Override
	public String[] getResults() {
		results[0] = Integer.toString(count);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		count=0;
		super.reset();
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
