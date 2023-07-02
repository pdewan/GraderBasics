package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public abstract class AbstractEventCollector extends AbstractCollector{

	protected int eventCount=0;
	protected List<String> workingResults = new ArrayList<String>();
	private final String [] set_headers = {"case_id,timestamp,activity,user"};
	
	@Override
	public boolean requiresStudentName() {
		return true;
	}
	
	@Override
	public boolean otherCollectorCompatable() {
		return false;
	}	

	@Override
	public String[] getResults() {
		String [] retval = new String[workingResults.size()];
		workingResults.toArray(retval);
		return retval;
	}
	
	@Override
	public void reset() {
		this.headers=set_headers;
		workingResults=new ArrayList<String>();
		super.reset();
	}
	
	@Override
	public String[] getHeaders() {
		eventCount=0;
		return set_headers;
	}
	
	protected void addActivity(String activity, String date) throws ParseException {
		String case_id = Integer.toString(eventCount);
		eventCount++;
		workingResults.add(case_id+","+convertDate(date)+","+activity+","+studentName+"\n");
	}
	
	private static final SimpleDateFormat eventDateFormat=new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
	protected String convertDate(String date) throws ParseException {
		Date conv = dateFormat.parse(date);
		String formatted = eventDateFormat.format(conv);
		return formatted;
	}
	private static final SimpleDateFormat eventDateFormatHH=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	protected String convertDateHH(String date) throws ParseException {
		Date conv = dateFormat.parse(date);
		String formatted = eventDateFormat.format(conv);
		return formatted;
	}
	
	@Override
	protected String getHeaderPhrase() {
		return null;
	}
	
	protected List<String> testsWorkedOn(String modifier, String []... testPools){
		List<String> retval = new ArrayList<String>();
		for(String [] testPool:testPools) 
			for(String test:testPool)
				if(test.matches(".*"+modifier))
					retval.add(test.replaceAll(modifier, ""));
		return retval;
	}
	
	protected void removeLastActivity() {
		workingResults.remove(workingResults.size()-1);
		eventCount--;
	}
	
	
}
