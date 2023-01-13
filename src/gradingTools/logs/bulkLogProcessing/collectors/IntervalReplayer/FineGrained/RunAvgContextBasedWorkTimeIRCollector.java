package gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.FineGrained;

import java.io.File;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;
import gradingTools.logs.bulkLogProcessing.collectors.Collector;

public class RunAvgContextBasedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector{

	public RunAvgContextBasedWorkTimeIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		generateHeaders();
		reset();
	}
	
	private int numRuns;
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		super.logData(data);
		
		int runs = Integer.parseInt(data[Collector.SESSION_NUMBER_INDEX])+1;
		if(runs!=numRuns)
			numRuns=runs;
		
	}
	
	@Override
	public String[] getResults() {
		double workingResults = replayer.getWorkTime(this.studentProject, this.startTime, this.currentTestTime)[0];
		results[0]=Double.toString(workingResults/numRuns);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		numRuns=0;
		super.reset();
	}
	
	@Override
	protected String getHeaderPhrase() {
		return "Avg Test Time Context Based Work Time";
	}

	@Override
	protected void generateHeaders() {
		headers = new String[1];
		headers[0]=getHeaderPhrase();
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}
	
}
