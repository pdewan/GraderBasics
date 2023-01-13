package gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class TestingSetStatisicsCollector extends AbstractCollector{
	
	protected double [] workingResults = new double[3];
	private final int totalWorkingSetIndex=0,totalKnownRunsIndex=1,studentAverageIndex=2,errorIndex=3;
	private final String [] headers_ = {
			"Total in Testing Set",
			"Total known Testing Set runs",
			"Student Testing Set Average",
			"Erroneous Testing Set Suite Names"
	};
	
	public TestingSetStatisicsCollector(){
		reqPass=1;
		headers=headers_;
		reset();
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		int suiteSize=0;
		try {
			suiteSize = suiteMapping.getSuiteInstances(data[TEST_SUITE_INDEX]).size();
		}catch(Exception e) {
			workingResults[errorIndex] ++;
			return;
		}
		if(suiteSize>0){
			workingResults[totalWorkingSetIndex] += suiteSize;
			workingResults[totalKnownRunsIndex] ++;
		}else 
			throw new IllegalArgumentException("Error accessing suite");
		
	}
	
	@Override
	public String[] getResults() {
		workingResults[studentAverageIndex] = workingResults[totalWorkingSetIndex] / workingResults[totalKnownRunsIndex];
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[headers.length];
		super.reset();
	}
	
	
	@Override
	public void setTestNames(String[] names) {
		testNames=names;
		reset();
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}

	@Override
	protected String getHeaderPhrase() {
		return null;
	}

	@Override
	public String[] getHeaders() {
		return headers;
	}

	@Override
	public boolean requiresSuiteMapping() {
		return true;
	}
	
}
