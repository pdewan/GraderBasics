package gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class KnownTimeCollector extends AbstractCollector{
	
	private String lastTime=null;
	private List<String> workingResults;

	
	public KnownTimeCollector() {
		workingResults=new ArrayList<String>();
		String [] header = {"Time Between Tests"};
		headers=header;
		reqPass=1;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		
		String time=data[TIME_TESTED_INDEX];
		if(lastTime!=null){
			try {
				workingResults.add(Long.toString(secondsBetween(lastTime, time))+"\n");
				
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Invalid formatting");
			}
		}
		
		lastTime=time;
	}

	@Override
	public String[] getResults() {
		String [] retval = new String[workingResults.size()];
		workingResults.toArray(retval);
		return retval;
	}
	
	@Override
	public void reset() {
		workingResults=new ArrayList<String>();
		lastTime=null;
		super.reset();
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}

	@Override
	protected String getHeaderPhrase() {
		return null;
	}

	@Override
	public boolean otherCollectorCompatable() {
		return false;
	}
	
}
