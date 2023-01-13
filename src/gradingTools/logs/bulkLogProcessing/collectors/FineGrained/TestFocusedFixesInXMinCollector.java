package gradingTools.logs.bulkLogProcessing.collectors.FineGrained;

import java.text.ParseException;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class TestFocusedFixesInXMinCollector extends AbstractCollector{
	
	private int [] workingResults;
	private String lastTime=null, lastRun="";
	private long lastTimeBetween=Long.MAX_VALUE;
	private final double timeBetween;
	private final String header;
	
	public TestFocusedFixesInXMinCollector(double minutes){
		this(" had X fixes in "+minutes+" minutes",minutes);
	}
	public TestFocusedFixesInXMinCollector(String header, double minutes) {
		this.header=header;
		timeBetween=minutes*60;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		
		if(!lastRun.equals(data[SESSION_NUMBER_INDEX])) {	
			if(lastTime==null) {
				lastTime=data[TIME_TESTED_INDEX];
				lastRun=data[SESSION_NUMBER_INDEX];
				updateTests(data);
				return;
			}
			
			try {
				lastTimeBetween = this.secondsBetween(lastTime,data[TIME_TESTED_INDEX]);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Date Parsing Error");
			}
			lastTime=data[TIME_TESTED_INDEX];
			lastRun=data[SESSION_NUMBER_INDEX];
		}
		updateTests(data);
		if(lastTimeBetween<=this.timeBetween) {
			List<String> improvedTests = getImprovedTests();
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<improvedTests.size();j++)
					if(testNames[i].equals(improvedTests.get(j)))
						workingResults[i]++;
		}
			

	}
	
	@Override
	public String[] getResults() {
		String [] results = new String[workingResults.length];
		for(int i=0;i<results.length;i++)
			results[i]=Integer.toString(workingResults[i]);
		this.results = results;
		return super.getResults();
	}
	
	@Override
	public void reset() {
		lastRun="";
		lastTimeBetween=Long.MAX_VALUE;
		lastTime=null;
		workingResults = new int[testNames.length];
		super.reset();
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}

	@Override
	protected String getHeaderPhrase() {
		return header;
	}
}
