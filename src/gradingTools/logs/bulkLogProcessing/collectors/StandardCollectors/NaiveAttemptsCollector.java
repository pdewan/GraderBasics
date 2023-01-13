package gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors;

import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class NaiveAttemptsCollector extends AbstractCollector{
	
	protected double [] workingResults;
	private final String headerPhrase;
	
	public NaiveAttemptsCollector(){
		this(" naive attempts");
		
	}
	public NaiveAttemptsCollector(String header) {
		headerPhrase=header;
		reqPass=1;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {

		List<String> movedTests = suiteMapping.getSuiteInstances(data[TEST_SUITE_INDEX]);
		String [] finishedTests = {};
		
		try {
			finishedTests = new String[movedTests.size()];
		}catch(Exception e) {
			return;
		}
		
		
		movedTests.toArray(finishedTests);
		
		if(finishedTests.length>0){
			double amount = 1.0/finishedTests.length;
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", "")))
						workingResults[i]+=amount;
		}else 
			throw new IllegalArgumentException("Error accessing suite");
	}
	
	@Override
	public String[] getResults() {
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[testNames.length];
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
	public boolean requiresSuiteMapping() {
		return true;
	}

}
