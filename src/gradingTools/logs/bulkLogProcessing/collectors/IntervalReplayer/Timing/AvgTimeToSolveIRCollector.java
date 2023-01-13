package gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;

public class AvgTimeToSolveIRCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	private long totalFixedTime=0,totalContextTime=0;
	private double numberTests=0;
	private Map<String,Date> brokenTests = new HashMap<>();
	
	public AvgTimeToSolveIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		this("",semesterFolderLocation, pathToStudents);
	}
	
	public AvgTimeToSolveIRCollector(String header, File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		headerPhrase=header;
		generateHeaders();
		reset();
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
			for(String passed:data[TEST_PASS_INDEX].split(" ")) {
				if(!passed.matches(".*\\+")) continue;
				
				passed=passed.replaceAll(movementMatchString, "");
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
					totalContextTime+=times[0];
					totalFixedTime+=times[1];
					numberTests++;
					
				}
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("Error when parsing date");
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		totalFixedTime=0;
		numberTests=0;
		totalContextTime=0;
		brokenTests = new HashMap<>();
	}
	
	@Override
	public String[] getResults() {
		if(numberTests>0) {
			results[0]=Double.toString(totalFixedTime/numberTests);
			results[1]=Double.toString(totalContextTime/numberTests);
		}else {
			results[0]="NaN";
			results[1]="NaN";
		}
		return results;
	}

	@Override
	protected String getHeaderPhrase() {
		return headerPhrase;
	}

	@Override
	protected void generateHeaders() {
		headers = new String[2];
		headers[0]="Fixed Time Average Fix Time";
		headers[1]="Context Time Average Fix Time";
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}
	
}
