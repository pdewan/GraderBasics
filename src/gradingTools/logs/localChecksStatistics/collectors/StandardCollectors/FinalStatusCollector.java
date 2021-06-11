package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public class FinalStatusCollector extends AbstractCollector{
	
	public FinalStatusCollector(){
		this.reqPass = Integer.MAX_VALUE;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String [][] categories={
				data[TEST_PASS_INDEX].split(" "),
				data[TEST_PARTIAL_INDEX].split(" "),
				data[TEST_FAIL_INDEX].split(" "),
				data[TEST_UNTESTED_INDEX].split(" "),
		};
		
		for(int i=0;i<testNames.length;i++){
			String testName=testNames[i];
			if(contains(categories[0],testName)||contains(categories[0],testName+"+"))
				results[i]="Pass";
			else if(contains(categories[1],testName)||contains(categories[1],testName+"+")||contains(categories[1],testName+"-"))
				results[i]="Partial";
			else if(contains(categories[2],testName)||contains(categories[2],testName+"+")||contains(categories[2],testName+"-"))
				results[i]="Fail";	
			else if(contains(categories[3],testName))
				results[i]="Untested";		
			else
				results[i]="Missing Test";
				//throw new IllegalArgumentException("TEST MISSING");
		}
		
	}

	@Override
	public boolean requiresTestNames() {
		return true;
	}

	@Override
	protected String getHeaderPhrase() {
		return " finished as";
	}

}
