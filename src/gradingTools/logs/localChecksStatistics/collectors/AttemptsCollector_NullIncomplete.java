package gradingTools.logs.localChecksStatistics.collectors;

import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.AttemptsCollector;

public class AttemptsCollector_NullIncomplete extends AttemptsCollector{
	
	public AttemptsCollector_NullIncomplete(){
		reqPass=1;
	}
	
	@Override
	public String[] getResults() {
		for(int i=0;i<workingResults.length;i++)
			results[i]=workingResults[i]==0?"":Double.toString(workingResults[i]);
		return results;
	}

	@Override
	protected String getHeaderPhrase() {
		return "";
	}


}
