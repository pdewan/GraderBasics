package gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing;

import java.io.File;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;

public class FixedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	
	public FixedWorkTimeIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		this("Fixed Work Time",semesterFolderLocation, pathToStudents);
	}
	
	public FixedWorkTimeIRCollector(String header, File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		headerPhrase=header;
		generateHeaders();
		reset();
	}
	
	@Override
	public String[] getResults() {
		long result = this.replayer.getWorkTime(this.studentProject,this.startTime,this.lastTestTime)[1];
		results[0] = Long.toString(result);
		return results;
	}

	@Override
	protected String getHeaderPhrase() {
		return headerPhrase;
	}

	@Override
	protected void generateHeaders() {
		headers = new String[1];
		headers[0]=getHeaderPhrase();
	}
	
}
