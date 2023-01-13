package gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;

public class AvgTestFocusedTimeToSolveIRCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	private long [][] workingResults;
	private Map<String,Date> brokenTests = new HashMap<>();
	
	public AvgTestFocusedTimeToSolveIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		this("",semesterFolderLocation, pathToStudents);
	}
	
	public AvgTestFocusedTimeToSolveIRCollector(String header, File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		headerPhrase=header;
	}
	

	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		updateTests(data);
		try {		
			for(String broken:combineArrays(data[TEST_FAIL_INDEX].split(" "),data[TEST_PARTIAL_INDEX].split(" "))) {
				broken = broken.replaceAll(movementMatchString, "");
				if(!brokenTests.containsKey(broken)) {
					brokenTests.put(broken,parseDate(data[TIME_TESTED_INDEX]));
				}
			}
			if(brokenTests.isEmpty()) return;
			
			Date currentTime = parseDate(data[TIME_TESTED_INDEX]);
			Map<Long,Long[]> alreadyCalculated = new HashMap<>();
			
			String [] passedTests=data[TEST_PASS_INDEX].split(" ");
			
			
			for(int j=0;j<passedTests.length;j++) {
				if(!passedTests[j].matches(".*\\+")) continue;
				String passed=passedTests[j].replaceAll(movementMatchString, "");
				for(int i=0;i<testNames.length;i++) {
					if(testNames[i].equals(passed)) {
						if(brokenTests.containsKey(passed)) {
							Date timeFound = brokenTests.remove(passed);
							Long [] times=new Long[2];
							if(alreadyCalculated.containsKey(timeFound.getTime())) {
								times=alreadyCalculated.get(timeFound.getTime());
							}else {
								long [] time = replayer.getWorkTime(studentProject, timeFound.getTime(), currentTime.getTime());
								times[0]=time[0];
								times[1]=time[1];
								alreadyCalculated.put(timeFound.getTime(), times);
							}
							workingResults[i][0]+=times[0];
							workingResults[i][1]+=times[1];
							workingResults[i][2]++;
						}
						break;
					}	
				}
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("Error when parsing date");
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		workingResults=new long [testNames.length*2][3];
		brokenTests = new HashMap<>();
	}
	
	@Override
	public String[] getResults() {
		int numTests=testNames.length;
		for(int i=0;i<numTests;i++) {
			if(workingResults[i][2]>0) {
				results[i]=Double.toString(workingResults[i][1]/(double)(workingResults[i][2]));
				results[i+numTests]=Double.toString(workingResults[i][0]/(double)(workingResults[i][2]));
			}else {
				results[i]="NaN";
				results[i+numTests]="NaN";
			}
		}
		return results;
	}

	@Override
	protected String getHeaderPhrase() {
		return headerPhrase;
	}

	@Override
	protected void generateHeaders() {
		headers = new String[testNames.length*2];
		int numTests=testNames.length;
		for(int i=0;i<numTests;i++) {
			headers[i]=testNames[i]+" Avg Fixed Time to Fix";
			headers[i+numTests]=testNames[i]+" Avg Context Time to Fix";
		}
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}
	
}
