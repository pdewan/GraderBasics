package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public class WorkTimeStatisticsCollector extends AbstractCollector{

	private final String movementMatchingRegex = ".*[\\+-]";

	private String lastTime=null;
	private List<List<Double>> workingResults;
	private long currentTime=0;
	
	public WorkTimeStatisticsCollector(){
		reqPass=1;
	}

	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		
		String time=data[TIME_TESTED_INDEX];
		if(lastTime!=null){
			try {
				currentTime+=secondsBetween(lastTime, time);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Invalid formatting");
			}
		}
		
		String [] finishedTests = getMatches(movementMatchingRegex,data[TEST_PASS_INDEX].split(" "),data[TEST_PARTIAL_INDEX].split(" "));
		if(finishedTests.length>0){
			double splitTime = currentTime==0?0:(currentTime/(double)finishedTests.length);
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", "")))
						workingResults.get(i).add(splitTime);
			currentTime=0;
		}
		lastTime=time;
	}
	
	@Override
	public String[] getResults() {
		List<String> results = new ArrayList<String>();
		StringBuilder line;
		
		for(int i=0;;i++) {
			boolean finishFlag = true;
			line=new StringBuilder();
			for(List<Double> data:workingResults)
				if(i>=data.size()) 
					line.append(",");
				else {
					finishFlag=false;
					line.append(data.get(i)+",");
				}	
			if(finishFlag)
				break;
			line.append("\n");
			results.add(line.toString());
		}
		
		String [] retval = new String[results.size()];
		results.toArray(retval);
		return retval;
	}
	
	@Override
	public void reset() {
		currentTime=0;
		workingResults=new ArrayList<List<Double>>();
		for(int i=0;i<testNames.length;i++)
			workingResults.add(new ArrayList<Double>());
		lastTime=null;
		super.reset();
	}

	@Override
	protected void generateHeaders() {
		StringBuilder line = new StringBuilder();
		for(int i=0;i<testNames.length;i++)
			line.append(testNames[i]+(i==testNames.length-1?"":","));
		String [] header = {line.toString()};
		this.headers=header;
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}


	@Override
	protected String getHeaderPhrase() {
		return " this should not be seen";
	}
	
	@Override
	public boolean otherCollectorCompatable() {
		return false;
	}
	
}
