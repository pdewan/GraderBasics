package gradingTools.logs.localChecksStatistics.collectors;

public abstract class AbstractStateChangeCollector extends AbstractCollector{

	private final String headerPhrase,modifier;
	private int [] workingResults;
	
	
	public AbstractStateChangeCollector(String header,String modifier) {
		headerPhrase=header;
		reqPass=1;
		this.modifier = modifier;
	}
	
	
	protected void check(String [] ... testPools) {
		for(String [] testPool:testPools) {
			String [] finishedTests = getMatches(".*"+modifier,testPool);
			if(finishedTests.length>0)
				for(int i=0;i<testNames.length;i++)
					for(int j=0;j<finishedTests.length;j++)
						if(testNames[i].equals(finishedTests[j].replaceAll(modifier, "")))
							workingResults[i]++;
		}
	}
	
	@Override
	public String[] getResults() {
		for(int i=0;i<workingResults.length;i++)
			results[i]=Integer.toString(workingResults[i]);
		return super.getResults();
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
	public void reset() {
		workingResults=new int[testNames.length];
		super.reset();
	}
	
}
