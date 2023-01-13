package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

public class TestWorkingEvent extends AbstractEventCollector{

	private String lastDate=null;
	private final String working="_workingOn", notWorking="_notWorkingOn";

	
	public TestWorkingEvent() {
		reqPass=1;
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String testDate = data[TIME_TESTED_INDEX];
		String [] finishedTests = getMatches(movementMatchingRegex,data[TEST_PASS_INDEX].split(" "),data[TEST_PARTIAL_INDEX].split(" "));
		if(finishedTests.length>0){
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", ""))) {
						try {
							if(lastDate==null) 
								addActivity(testNames[i]+working,testDate);
							else 
								addActivity(testNames[i]+working,lastDate);
							addActivity(testNames[i]+notWorking,testDate);
						}catch(Exception e) {
							throw new IllegalArgumentException("Error in date formatting");
						}
					}
			lastDate=testDate;
		}
		
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
