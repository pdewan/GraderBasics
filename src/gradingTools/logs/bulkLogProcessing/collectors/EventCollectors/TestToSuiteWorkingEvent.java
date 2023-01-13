package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestToSuiteWorkingEvent extends AbstractEventCollector{

	private String lastWorkingSuite=null,lastDate=null;
	private final String working="_workingOn", notWorking="_notWorkingOn";
	private boolean multirun=false;
	
	public TestToSuiteWorkingEvent() {
		reqPass=1;
	}
	
	
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String [] passedTests = data[TEST_PASS_INDEX].split(" ");
		String [] partialTests = data[TEST_PARTIAL_INDEX].split(" ");
		String [] failedTests = data[TEST_FAIL_INDEX].split(" ");
		String testDate = data[TIME_TESTED_INDEX];
		
		List<String> stateChanged = testsWorkedOn("[\\+-]",passedTests,partialTests,failedTests);
		
		for(int i=0;i<stateChanged.size();i++) 
			if(!suiteMapping.containsTestMapping(stateChanged.get(i))) {
				String str = stateChanged.remove(i);
//				System.err.println("TEST NOT FOUND IN MAPPING: " + str);
				i--;
			}

		if(stateChanged.size()==0) 
			return;
		
		String workingOn=null;
		
		if(stateChanged.size()==1) {
			workingOn=stateChanged.remove(0);
		}else {
			Set<String> set=new HashSet<String>(suiteMapping.getTestInstances(stateChanged.get(0)));
			
			for(int i=1;i<stateChanged.size();i++)
				if(!suiteMapping.containsTestMapping(stateChanged.get(i)))
					continue;
				else 
					set.retainAll(suiteMapping.getTestInstances(stateChanged.get(i)));
				
			
			
			if(set.size()==0) 
				workingOn = data[TEST_SUITE_INDEX];
			else if(set.size()==1) 
				workingOn = new ArrayList<String>(set).get(0);
			else {
				int smallest=Integer.MAX_VALUE;
				for(String suite:set) {
					int size = suiteMapping.getSuiteInstances(suite).size();
					if(size<smallest) {
						smallest=size;
						workingOn=suite;
					}
				}
			}
		}
			
		try {
			if(lastWorkingSuite==null) {
				addActivity(workingOn+working, testDate);
				addActivity(workingOn+notWorking, testDate);	
				lastWorkingSuite=workingOn;
				lastDate=testDate;
			}else if(!workingOn.equals(lastWorkingSuite)) {
				if(multirun) {
					addActivity(lastWorkingSuite+notWorking,lastDate);
					multirun=false;
				}
				addActivity(workingOn+working,lastDate);
				addActivity(workingOn+notWorking,testDate);
				lastWorkingSuite=workingOn;
				lastDate=testDate;
			}else {
				if(!multirun) 
					removeLastActivity();
				multirun=true;
				lastDate=testDate;
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("Error parsing date");
		}
		

	}

	
	
	@Override
	public void reset() {
		super.reset();
		lastWorkingSuite=null;
		lastDate=null;
		multirun=false;
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}

	@Override
	public boolean requiresSuiteMapping() {
		return true;
	}
	
}
