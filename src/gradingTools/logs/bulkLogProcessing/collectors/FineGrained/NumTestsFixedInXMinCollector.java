package gradingTools.logs.bulkLogProcessing.collectors.FineGrained;

import java.text.ParseException;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class NumTestsFixedInXMinCollector extends AbstractCollector{
	
	private int currentAmount=0;
	private String lastTime=null, lastRun="";
	private long lastTimeBetween=Long.MAX_VALUE;
	private final double timeBetween;
	private final String header;
	
	public NumTestsFixedInXMinCollector(double minutes){
		this("Fixed in "+minutes+" minutes",minutes);
	}
	public NumTestsFixedInXMinCollector(String header, double minutes) {
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
		if(lastTimeBetween<=this.timeBetween)
			currentAmount+=this.getImprovedTests().size();
//		System.out.println("fixed:"+data[0]+" "+currentAmount);
	}
	
	@Override
	public String[] getResults() {
		String [] results = {Integer.toString(currentAmount)};
		this.results = results;
		return super.getResults();
	}
	
	@Override
	public void reset() {
		lastRun="";
		currentAmount=0;
		lastTimeBetween=Long.MAX_VALUE;
		lastTime=null;
		super.reset();
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}

	protected void generateHeaders() {
		String [] headers = {getHeaderPhrase()};
		this.headers=headers;
	}
	
	@Override
	protected String getHeaderPhrase() {
		return header;
	}
}
