package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import java.text.ParseException;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public class WorkTimeCollector extends AbstractCollector{

	
	private final long breakTime;
	
	private String lastTime=null;
	private double [] workingResults;
	private long currentTime=0;
	private final String headerPhrase;
	private final boolean initialBreak;
	
	public WorkTimeCollector(long breakTime) {
		this(breakTime,false);
	}
	
	public WorkTimeCollector(long breakTime, boolean includeInitBreak){
		this(" was worked on for x seconds",breakTime,includeInitBreak);
	}

	public WorkTimeCollector(String header, long breakTime, boolean includeInitBreak) {
		initialBreak=includeInitBreak;
		headerPhrase=header;
		reqPass=1;
		this.breakTime=breakTime;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		
		String time=data[TIME_TESTED_INDEX];
		if(lastTime!=null){
			try {
				long difference = secondsBetween(lastTime, time);
				currentTime+=difference>breakTime?(initialBreak?breakTime:0):difference;
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Invalid formatting");
			}
		}
		
		String [] finishedTests = getMatches(movementMatchingRegex,data[TEST_PASS_INDEX].split(" "),data[TEST_PARTIAL_INDEX].split(" "));
		if(finishedTests.length>0){
			double splitTime = (currentTime==0?(initialBreak?breakTime:0):currentTime)/(double)finishedTests.length;
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", "")))
						workingResults[i]+=splitTime;
			currentTime=0;
		}
		lastTime=time;
	}
	
	@Override
	public String[] getResults() {
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		currentTime=0;
		workingResults=new double[testNames.length];
		lastTime=null;
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
