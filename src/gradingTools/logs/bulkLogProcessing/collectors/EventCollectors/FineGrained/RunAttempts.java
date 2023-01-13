package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors.FineGrained;

import java.util.ArrayList;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class RunAttempts extends AbstractCollector{
	
	protected double [] workingResults;
	private int runs=0;
	private final String headerPhrase;
	private String currentSession="";
	private List<String> currentWorkingSet;
	
	public RunAttempts(){
		this(" attempts");
	}
	public RunAttempts(String header) {
		headerPhrase=header;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		updateTests(data);
		
		if(!currentSession.equals(data[SESSION_NUMBER_INDEX])) {
			currentSession = data[SESSION_NUMBER_INDEX];
			runs++;
			if(currentWorkingSet.size()>0) 
				updateWorkingResult();
			
		}

		List<String> changedTests = getChangedTests();
		currentWorkingSet.addAll(changedTests);
	}
	
	private void updateWorkingResult() {
		String [] finishedTests = new String[currentWorkingSet.size()];
		currentWorkingSet.toArray(finishedTests);
		currentWorkingSet = new ArrayList<>();
		
		if(finishedTests.length>0){
			double amount = (double)runs/finishedTests.length;
			for(int j=0;j<finishedTests.length;j++) {
				String finishedTest = finishedTests[j].replace("+", "").replace("-", "");
				for(int i=0;i<testNames.length;i++)
					if(testNames[i].equals(finishedTest)) {
						workingResults[i]+=amount;
						break;
					}
						
			}
			runs=0;
		}	
	}
	
	@Override
	public String[] getResults() {
		double amount=0;
		//If there are any in the working set at this point they must've occurred during the final run i.e. no time to work on extras
		if(currentWorkingSet.size()>0) 
			updateWorkingResult();
		else {
			int numNeverAttempted=0;
			for(int i=0;i<workingResults.length;i++)
				if(workingResults[i]==0)
					numNeverAttempted++;
			amount=-1.0*((double)runs/numNeverAttempted);
		}
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]==0?amount:workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[testNames.length];
		currentWorkingSet = new ArrayList<>();
		currentSession="";
		runs=0;
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
