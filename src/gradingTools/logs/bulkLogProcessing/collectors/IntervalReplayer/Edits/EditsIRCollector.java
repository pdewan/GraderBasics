package gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Edits;

import java.io.File;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;

public class EditsIRCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String [] header= {
			"Number of Incerts",
			"Characters inserted",
			"Number of Delete",
			"Characters Deleted"
	};
	
	public EditsIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
		generateHeaders();
		reset();
	}
	
	@Override
	public String[] getResults() {
		int [] result = this.replayer.getEdits(this.studentProject,this.startTime,this.lastTestTime);
		
		for(int i=0;i<results.length;i++)
			results[i] = Integer.toString(result[i]);
		
		return results;
	}

	@Override
	protected String getHeaderPhrase() {
		return null;
	}
	
	@Override
	protected void generateHeaders() {
		headers = header;
	}

}
