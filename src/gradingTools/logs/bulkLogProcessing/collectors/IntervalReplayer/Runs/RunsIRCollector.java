package gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Runs;

import java.io.File;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;

public class RunsIRCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	
	public RunsIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		this("Context Based Time",semesterFolderLocation, pathToStudents);
	}
	
	public RunsIRCollector(String header, File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		headerPhrase=header;
		generateHeaders();
		reset();
	}
	
	@Override
	public String[] getResults() {
		int result = this.replayer.getRuns(this.studentProject,this.startTime,this.lastTestTime);
		results[0] = Integer.toString(result);
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
