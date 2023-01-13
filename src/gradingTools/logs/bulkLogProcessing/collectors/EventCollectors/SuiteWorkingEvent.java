package gradingTools.logs.bulkLogProcessing.collectors.EventCollectors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuiteWorkingEvent extends AbstractEventCollector{

	private String lastWorkingSuite=null,lastDate=null;
	private final String working="_workingOn", notWorking="_notWorkingOn";
	private boolean multirun=false;
	
	public SuiteWorkingEvent() {
		reqPass=1;
	}
	
	
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String [] passedTests = data[TEST_PASS_INDEX].split(" ");
		String [] partialTests = data[TEST_PARTIAL_INDEX].split(" ");
		String [] failedTests = data[TEST_FAIL_INDEX].split(" ");
		String testDate = data[TIME_TESTED_INDEX];
		
		List<String> stateChanged = testsWorkedOn("[\\+-]",passedTests,partialTests,failedTests);
		while(stateChanged.size()>0 && !suiteMapping.containsTestMapping(stateChanged.get(0)))
			stateChanged.remove(0);

		if(stateChanged.size()==0) 
			return;
		
		
		
		Set<String> set=new HashSet<String>(suiteMapping.getTestInstances(stateChanged.get(0)));
	//	boolean allTestsPassing=matches(passedTests, stateChanged.get(0)+"\\+");
		
		for(int i=1;i<stateChanged.size();i++)
			if(!suiteMapping.containsTestMapping(stateChanged.get(i)))
				continue;
			else {
				set.retainAll(suiteMapping.getTestInstances(stateChanged.get(i)));
	//			allTestsPassing = allTestsPassing&&matches(passedTests, stateChanged.get(i)+"\\+");
			}
		
		String workingSuite=null;
		if(set.size()==0) 
			workingSuite = data[TEST_SUITE_INDEX];
		else if(set.size()==1) 
			workingSuite= new ArrayList<String>(set).get(0);
		else {
			int smallest=Integer.MAX_VALUE;
			for(String suite:set) {
				int size = suiteMapping.getSuiteInstances(suite).size();
				if(size<smallest) {
					smallest=size;
					workingSuite=suite;
				}
				if(size==smallest&&workingSuite.contains("Assignment")&&!workingSuite.equals(data[TEST_SUITE_INDEX])) 
					workingSuite=suite;
			}
		}
		
		try {
			if(lastWorkingSuite==null) {
				addActivity(workingSuite+working, testDate);
				addActivity(workingSuite+notWorking, testDate);	
				lastWorkingSuite=workingSuite;
				lastDate=testDate;
			}else if(!workingSuite.equals(lastWorkingSuite)) {
				if(multirun) {
					addActivity(lastWorkingSuite+notWorking,lastDate);
					multirun=false;
				}
				addActivity(workingSuite+working,lastDate);
				addActivity(workingSuite+notWorking,testDate);
				lastWorkingSuite=workingSuite;
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
