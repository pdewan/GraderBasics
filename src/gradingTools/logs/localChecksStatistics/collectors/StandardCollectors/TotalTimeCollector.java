package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import java.text.ParseException;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public class TotalTimeCollector extends AbstractCollector{

	private final String headerPhrase;
	private String initial=null,last=null;
	
	public TotalTimeCollector() {
		this("Time between first and last test");
		reqPass=1;
		results = new String[1];
		results[0]=null;
		generateHeaders();
	}
	
	public TotalTimeCollector(String header) {
		headerPhrase=header;
		
	}
	
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String date = data[TIME_TESTED_INDEX];
		if(initial==null)
			initial=date;
		last =date;
		
		try {
			results[0]=Long.toString(secondsBetween(initial,last));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Invalid formatting");
		}
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
	public void reset() {
		super.reset();
		initial=null;
		last=null;
	}
	
	@Override
	protected void generateHeaders() {
		headers = new String[1];
		headers[0]=getHeaderPhrase();
	}
}
