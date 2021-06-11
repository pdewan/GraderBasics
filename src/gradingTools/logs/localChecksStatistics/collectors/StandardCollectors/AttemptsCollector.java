package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public class AttemptsCollector extends AbstractCollector{

	private final String movementMatchingRegex = ".*[\\+-]";
	
	protected double [] workingResults;
	private int attempts=0;
	private final String headerPhrase;
	
	public AttemptsCollector(){
		this(" was attempted x times");
		
	}
	public AttemptsCollector(String header) {
		headerPhrase=header;
		reqPass=1;
	}
	
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String [] passedTests = data[TEST_PASS_INDEX].split(" ");
		String [] partialTests = data[TEST_PARTIAL_INDEX].split(" ");

		attempts++;
		String [] finishedTests = getMatches(movementMatchingRegex,passedTests,partialTests);

		if(finishedTests.length>0){
			double amount = (double)attempts/finishedTests.length;
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", "")))
						workingResults[i]+=amount;
			attempts=0;
		}
	}
	
	@Override
	public String[] getResults() {
		int numNeverAttempted=0;
		for(int i=0;i<workingResults.length;i++)
			if(workingResults[i]==0)
				numNeverAttempted++;
		double amount=-1.0*((double)attempts/numNeverAttempted);
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]==0?amount:workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[testNames.length];
		attempts=0;
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



}
