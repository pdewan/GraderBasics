package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

public class BreakEvent extends AbstractEventCollector{

	private String lastDate=null;
	private final String working="resumed working", notWorking="will start a break", start = "started testing", breakContinued="continues to break", workContinued="continues to work";
	private final int breakTime;
	
	private boolean wasLastWorking=true;
	
	public BreakEvent(int breakTime) {
		reqPass=1;
		this.breakTime=breakTime;
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String testDate = data[TIME_TESTED_INDEX];

		try {
			if(lastDate==null) 
				addActivity(start,testDate);
			else {
				boolean isBreak = secondsBetween(lastDate,testDate)>breakTime;
				if(wasLastWorking && isBreak) 
					addActivity(notWorking,lastDate);
				else if(!wasLastWorking && !isBreak) 
					addActivity(working,lastDate);
				else if(wasLastWorking&&!isBreak)
					addActivity(workContinued,lastDate);
				else
					addActivity(breakContinued,lastDate);
				
				wasLastWorking=!isBreak;
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("Error in date formatting");
		}
					
		lastDate=testDate;
		
		
	}

	@Override
	public void reset() {
		super.reset();
		lastDate=null;

	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}

	@Override
	public boolean requiresSuiteMapping() {
		return false;
	}

}
