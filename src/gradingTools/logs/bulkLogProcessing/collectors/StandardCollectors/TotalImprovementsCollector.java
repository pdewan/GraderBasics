package gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class TotalImprovementsCollector extends AbstractCollector {

	private int count;
	private final String headerPhrase;
	
	public TotalImprovementsCollector() {
		this("Number of Tests Improved");

	}
	
	public TotalImprovementsCollector(String header) {
		headerPhrase=header;
		reqPass=1;
		count=0;
		results = new String [1];
		generateHeaders();
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		this.updateTests(data);
		count+=this.getImprovedTests().size();
//		System.out.println("Total:"+data[0]+" "+count+" "+getImprovedTests());
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
		return true;
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
