package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import java.util.HashMap;
import java.util.List;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;


public class WorkingSetStatisicsCollector extends AbstractCollector{
	
	protected double [] workingResults = new double[3];
	private int attempts=0;
	private final int totalWorkingSetIndex=0,totalKnownRunsIndex=1,studentAverageIndex=2;
	private final String [] headers = {
			"Total in Working Set",
			"Total known Working Set runs",
			"Student Working Set Average"
	};
	
	public WorkingSetStatisicsCollector(){
		reqPass=1;
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		attempts++;
		updateTests(data);
		List<String> movedTests = getChangedTests();
		String [] finishedTests = new String[movedTests.size()];
		movedTests.toArray(finishedTests);
		
		if(finishedTests.length>0){
			workingResults[totalWorkingSetIndex] += finishedTests.length * attempts;
			workingResults[totalKnownRunsIndex] += attempts;
			attempts=0;
		}
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
		attempts=0;
		results=new String[headers.length];
		tests = new HashMap<String,State>();
	}
	
	
	@Override
	public void setTestNames(String[] names) {
		testNames=names;
		reset();
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}

	@Override
	protected String getHeaderPhrase() {
		return null;
	}

	@Override
	public String[] getHeaders() {
		return headers;
	}


}

